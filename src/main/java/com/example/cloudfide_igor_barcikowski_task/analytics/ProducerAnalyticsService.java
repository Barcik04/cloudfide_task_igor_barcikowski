package com.example.cloudfide_igor_barcikowski_task.analytics;

import com.example.cloudfide_igor_barcikowski_task.analytics.dto.*;
import com.example.cloudfide_igor_barcikowski_task.producer.Producer;
import com.example.cloudfide_igor_barcikowski_task.producer.ProducerRepository;
import com.example.cloudfide_igor_barcikowski_task.product.Product;
import com.example.cloudfide_igor_barcikowski_task.product.ProductRepository;
import com.example.cloudfide_igor_barcikowski_task.productattributes.ProductAttribute;
import com.example.cloudfide_igor_barcikowski_task.productattributes.ProductAttributeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// Business context:
// A product catalog company needs a producer analytics dashboard that provides insights into producer activity.
// The system should identify high-value producers, detect inactive producers who need reactivation campaigns,
// segment producers by behavior patterns, and automatically send promotional emails to inactive producers.
// The marketing team complains that generating producer analytics reports takes too long (sometimes over 5 minutes)
// and the system occasionally crashes or becomes unresponsive. The database has grown to contain:
//
// 500,000+ producers
// 2,000,000+ products
// 5,000,000+ product attributes
//
// Task:
// Analyze the provided Java code and identify all performance issues, architectural concerns, and potential system stability problems.
// Propose specific solutions for each issue you find.

@Service
public class ProducerAnalyticsService {

    @Autowired
    private ProducerRepository producerRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductAttributeRepository productAttributeRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private LogService logService;

    private static Map<String, ProducerAnalyticsDTO> reportCache = new HashMap<>();

    @Transactional
    public ProducerAnalyticsDTO generateProducerAnalytics() {
        List<Producer> allProducers = producerRepository.findAll();
        List<Product> allProducts = productRepository.findAll();
        List<ProductAttribute> allAttributes = productAttributeRepository.findAll();

        ProducerAnalyticsDTO analytics = new ProducerAnalyticsDTO();
        analytics.setHighValueProducers(findHighValueProducers(allProducers, allProducts, allAttributes));
        analytics.setInactiveProducers(findInactiveProducers(allProducers, allProducts));
        analytics.setProducerSegments(analyzeProducerSegments(allProducers, allProducts, allAttributes));

        sendReactivationEmails(analytics.getInactiveProducers());
        logDetailedAnalytics(allProducers, allProducts);

        reportCache.put("latest_report", analytics);

        return analytics;
    }

    private List<ProducerSummary> findHighValueProducers(List<Producer> producers,
                                                         List<Product> products,
                                                         List<ProductAttribute> attributes) {
        List<ProducerSummary> result = new ArrayList<>();

        for (Producer producer : producers) {
            BigDecimal totalRevenue = BigDecimal.ZERO;
            int totalProducts = 0;

            for (Product product : products) {
                if (product.getProducer().getId().equals(producer.getId())) {
                    totalProducts++;

                    for (ProductAttribute attribute : attributes) {
                        if (attribute.getProduct().getId().equals(product.getId())) {
                            totalRevenue = totalRevenue.add(new BigDecimal(attribute.getValue()));
                        }
                    }
                }
            }

            if (totalRevenue.compareTo(new BigDecimal("1000")) > 0) {
                ProducerSummary ps = new ProducerSummary();
                ps.setProducerId(producer.getId());
                ps.setProducerName(producer.getName());
                ps.setTotalRevenue(totalRevenue);
                ps.setTotalProducts(totalProducts);
                ps.setProducerSince(Date.from(producer.getCreatedAt()));
                result.add(ps);
            }
        }

        for (int i = 0; i < result.size() - 1; i++) {
            for (int j = i + 1; j < result.size(); j++) {
                if (result.get(i).getTotalRevenue().compareTo(result.get(j).getTotalRevenue()) < 0) {
                    ProducerSummary temp = result.get(i);
                    result.set(i, result.get(j));
                    result.set(j, temp);
                }
            }
        }

        return result.size() > 50 ? result.subList(0, 50) : result;
    }

    private List<ProducerSummary> findInactiveProducers(List<Producer> producers,
                                                         List<Product> products) {
        List<ProducerSummary> inactive = new ArrayList<>();
        Date sixMonthsAgo = new Date(System.currentTimeMillis() - (6L * 30 * 24 * 60 * 60 * 1000));

        for (Producer producer : producers) {
            Date lastProductDate = null;
            int totalProducts = 0;

            for (Product product : products) {
                if (product.getProducer().getId().equals(producer.getId())) {
                    totalProducts++;
                    Date productDate = Date.from(product.getCreatedAt());
                    if (lastProductDate == null || productDate.after(lastProductDate)) {
                        lastProductDate = productDate;
                    }
                }
            }

            if (totalProducts == 0 || (lastProductDate != null && lastProductDate.before(sixMonthsAgo))) {
                ProducerSummary ps = new ProducerSummary();
                ps.setProducerId(producer.getId());
                ps.setProducerName(producer.getName());
                ps.setLastProductDate(lastProductDate);
                ps.setTotalProducts(totalProducts);
                inactive.add(ps);
            }
        }

        return inactive;
    }

    private List<ProducerSegment> analyzeProducerSegments(List<Producer> producers,
                                                           List<Product> products,
                                                           List<ProductAttribute> attributes) {
        Map<String, List<ProducerSummary>> segments = new HashMap<>();

        for (Producer producer : producers) {
            int productCount = 0;
            BigDecimal totalRevenue = new BigDecimal(0.0);

            for (Product product : products) {
                if (product.getProducer().getId().equals(producer.getId())) {
                    productCount++;

                    for (ProductAttribute attribute : attributes) {
                        if (attribute.getProduct().getId().equals(product.getId())) {
                            totalRevenue = totalRevenue.add(new BigDecimal(attribute.getValue()));
                        }
                    }
                }
            }

            String segment = determineProducerSegment(productCount, totalRevenue, Date.from(producer.getCreatedAt()));

            ProducerSummary ps = new ProducerSummary();
            ps.setProducerId(producer.getId());
            ps.setProducerName(producer.getName());
            ps.setTotalProducts(productCount);
            ps.setTotalRevenue(totalRevenue);

            segments.computeIfAbsent(segment, k -> new ArrayList<>()).add(ps);
        }

        List<ProducerSegment> result = new ArrayList<>();
        for (Map.Entry<String, List<ProducerSummary>> entry : segments.entrySet()) {
            ProducerSegment segment = new ProducerSegment();
            segment.setSegmentName(entry.getKey());
            segment.setProducers(entry.getValue());
            segment.setProducerCount(entry.getValue().size());
            result.add(segment);
        }

        return result;
    }

    private String determineProducerSegment(int productCount, BigDecimal totalRevenue, Date registrationDate) {
        Date oneYearAgo = new Date(System.currentTimeMillis() - (365L * 24 * 60 * 60 * 1000));

        if (totalRevenue.compareTo(new BigDecimal("5000")) > 0 && productCount > 10) {
            return "VIP";
        } else if (totalRevenue.compareTo(new BigDecimal("1000")) > 0 && productCount > 5) {
            return "PREMIUM";
        } else if (registrationDate.after(oneYearAgo)) {
            return "NEW";
        } else {
            return "REGULAR";
        }
    }

    private void sendReactivationEmails(List<ProducerSummary> inactiveProducers) {
        for (ProducerSummary producer : inactiveProducers) {
            try {
                emailService.sendReactivationEmail(producer.getProducerId(), producer.getProducerName());
                Thread.sleep(100);
            } catch (Exception e) {
                System.err.println("Failed to send email to producer: " + producer.getProducerId());
            }
        }
    }

    private void logDetailedAnalytics(List<Producer> producers, List<Product> products) {
        ExecutorService executor = Executors.newFixedThreadPool(10);

        for (Producer producer : producers) {
            executor.submit(() -> {
                try {
                    calculateDetailedMetrics(producer, products);
                } catch (Exception e) {
                    throw new RuntimeException("error");
                }
            });
        }
    }

    private void calculateDetailedMetrics(Producer producer, List<Product> products) {
        List<String> metrics = new ArrayList<>();

        for (Product product : products) {
            if (product.getProducer().getId().equals(producer.getId())) {
                StringBuilder detailedLog = new StringBuilder();
                detailedLog.append("Producer: ").append(producer.getName());
                detailedLog.append(", Product: ").append(product.getId());
                detailedLog.append(", Name: ").append(product.getProductName());
                detailedLog.append(", Price: ").append(product.getPrice());
                detailedLog.append(", Date: ").append(product.getCreatedAt());
                metrics.add(detailedLog.toString());
            }
        }

        logService.logMetrics(producer.getId(), metrics);
    }

    @Scheduled(fixedRate = 300000)
    public void generateScheduledReports() {
        generateProducerAnalytics();
    }
}

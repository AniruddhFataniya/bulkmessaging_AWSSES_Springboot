package com.rishabhsoft.bulkmessaging.service;

import net.bytebuddy.asm.Advice;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.cloudwatch.CloudWatchClient;
import software.amazon.awssdk.services.cloudwatch.model.*;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class CloudWatchServiceImpl implements CloudWatchService{



    @Override
    public List<String> getMetrics(CloudWatchClient client) {

        DimensionFilter dimensionFilter = DimensionFilter.builder()
                .name("Email")
                .value("status")
                .build();

        List<String> metric = new ArrayList<>();
        ListMetricsRequest listMetricsRequest = ListMetricsRequest.builder()
                .namespace("AWS/SES")
                .dimensions(dimensionFilter)
                .build();

        ListMetricsResponse listMetricsResponse = client.listMetrics(listMetricsRequest);
        // listMetricsResponse.metrics();

        for(Metric met: listMetricsResponse.metrics()){
            System.out.println("Metric:"+met.metricName());
            metric.add(met.metricName());
        }


        return metric;
    }

    @Override
    public String getSentEmailCount(CloudWatchClient cloudWatchClient) {

       //Instant start = Instant.parse("2022-10-21T10:12:35.123456789Z").truncatedTo(ChronoUnit.MILLIS);
        Instant start = Instant.now().truncatedTo(ChronoUnit.MILLIS).minusSeconds(86400);
        Instant end = Instant.now().truncatedTo(ChronoUnit.MILLIS);


        Dimension dimension = Dimension.builder()
                .name("BulkEmail")
                .value("status")
                .build();

        Metric met = Metric.builder()
                .dimensions(dimension)
                .metricName("Send")
                .namespace("AWS/SES")
                .build();

        MetricStat metStat = MetricStat.builder()
                .stat("Sum")
                .period(86400)
                .metric(met)
                .build();

        MetricDataQuery dataQuery = MetricDataQuery.builder()
                .metricStat(metStat)
                .id("q1")
                .returnData(true)
                .build();

        List<MetricDataQuery> dq = new ArrayList<>();
        dq.add(dataQuery);

        GetMetricDataRequest getMetricDataRequest = GetMetricDataRequest.builder()
                .maxDatapoints(100)
                .startTime(start)
                .endTime(end)
                .metricDataQueries(dq)
                .build();

        GetMetricDataResponse response = cloudWatchClient.getMetricData(getMetricDataRequest);

        List<MetricDataResult> data = response.metricDataResults();
        //List<String> label = new ArrayList<>();

        List<Double>sendCount = new ArrayList<>();

        for(MetricDataResult item: data){
            //label.add(item.label())
            System.out.println("Label:"+item.label());
            System.out.println("id:"+item.id());
            System.out.println("status:"+item.statusCodeAsString());
            System.out.println(item.values());

            for (Double i: item.values()){
                sendCount.add(i);
            }


        }
        String sendCountString = sendCount.toString();


        return sendCountString;
    }

    @Override
    public String getDeliveredEmailCount(CloudWatchClient cloudWatchClient) {

        Instant start = Instant.now().truncatedTo(ChronoUnit.MILLIS).minusSeconds(86400);
        Instant end = Instant.now().truncatedTo(ChronoUnit.MILLIS);
        //Instant s = Instant.ofEpochSecond(Instant.now().getEpochSecond());

        System.out.println(start);
        System.out.println(end);

        Dimension dimension = Dimension.builder()
                .name("BulkEmail")
                .value("status")
                .build();


        Metric met = Metric.builder()
                .dimensions(dimension)
                .metricName("Delivery")
                .namespace("AWS/SES")
                .build();

        MetricStat metStat = MetricStat.builder()
                .stat("Sum")
                .period(86400)
                .metric(met)
                .build();

        MetricDataQuery dataQuery = MetricDataQuery.builder()
                .metricStat(metStat)
                .id("q1")
                .returnData(true)
                .build();

        List<MetricDataQuery> dq = new ArrayList<>();
        dq.add(dataQuery);

        GetMetricDataRequest getMetricDataRequest = GetMetricDataRequest.builder()
                .maxDatapoints(100)
                .startTime(start)
                .endTime(end)
                .metricDataQueries(dq)
                .build();

        GetMetricDataResponse response = cloudWatchClient.getMetricData(getMetricDataRequest);

        List<MetricDataResult> data = response.metricDataResults();


        List<Double>deliveredCount = new ArrayList<>();

        for(MetricDataResult item: data){
            //label.add(item.label())
            System.out.println("Label:"+item.label());
            System.out.println("id:"+item.id());
            System.out.println("status:"+item.statusCodeAsString());
            System.out.println(item.values());

            for (Double i: item.values()){
                deliveredCount.add(i);
            }


        }
        String deliveredCountString = deliveredCount.toString();


        return deliveredCountString;
    }

    @Override
    public String getOpenedEmailCount(CloudWatchClient cloudWatchClient) {
        Instant start = Instant.now().truncatedTo(ChronoUnit.MILLIS).minusSeconds(86400);
        Instant end = Instant.now().truncatedTo(ChronoUnit.MILLIS);
        //Instant s = Instant.ofEpochSecond(Instant.now().getEpochSecond());

        System.out.println(start);
        System.out.println(end);

        Dimension dimension = Dimension.builder()
                .name("BulkEmail")
                .value("status")
                .build();


        Metric met = Metric.builder()
                .dimensions(dimension)
                .metricName("Open")
                .namespace("AWS/SES")
                .build();

        MetricStat metStat = MetricStat.builder()
                .stat("Sum")
                .period(86400)
                .metric(met)
                .build();

        MetricDataQuery dataQuery = MetricDataQuery.builder()
                .metricStat(metStat)
                .id("q1")
                .returnData(true)
                .build();

        List<MetricDataQuery> dq = new ArrayList<>();
        dq.add(dataQuery);

        GetMetricDataRequest getMetricDataRequest = GetMetricDataRequest.builder()
                .maxDatapoints(100)
                .startTime(start)
                .endTime(end)
                .metricDataQueries(dq)
                .build();

        GetMetricDataResponse response = cloudWatchClient.getMetricData(getMetricDataRequest);

        List<MetricDataResult> data = response.metricDataResults();


        List<Double>openedCount = new ArrayList<>();

        for(MetricDataResult item: data){
            //label.add(item.label())
            System.out.println("Label:"+item.label());
            System.out.println("id:"+item.id());
            System.out.println("status:"+item.statusCodeAsString());
            System.out.println(item.values());

            for (Double i: item.values()){
                openedCount.add(i);
            }


        }
        String openedCountString = openedCount.toString();


        return openedCountString;
    }

    @Override
    public String getClickCount(CloudWatchClient cloudWatchClient) {

        Instant start = Instant.now().truncatedTo(ChronoUnit.MILLIS).minusSeconds(86400);
        Instant end = Instant.now().truncatedTo(ChronoUnit.MILLIS);

        Dimension dimension = Dimension.builder()
                .name("BulkEmail")
                .value("status")
                .build();


        Metric met = Metric.builder()
                .dimensions(dimension)
                .metricName("Click")
                .namespace("AWS/SES")
                .build();

        MetricStat metStat = MetricStat.builder()
                .stat("Sum")
                .period(86400)
                .metric(met)
                .build();

        MetricDataQuery dataQuery = MetricDataQuery.builder()
                .metricStat(metStat)
                .id("q1")
                .returnData(true)
                .build();

        List<MetricDataQuery> dq = new ArrayList<>();
        dq.add(dataQuery);

        GetMetricDataRequest getMetricDataRequest = GetMetricDataRequest.builder()
                .maxDatapoints(100)
                .startTime(start)
                .endTime(end)
                .metricDataQueries(dq)
                .build();

        GetMetricDataResponse response = cloudWatchClient.getMetricData(getMetricDataRequest);

        List<MetricDataResult> data = response.metricDataResults();


        List<Double>clickCount = new ArrayList<>();

        for(MetricDataResult item: data){
            //label.add(item.label())
            System.out.println("Label:"+item.label());
            System.out.println("id:"+item.id());
            System.out.println("status:"+item.statusCodeAsString());
            System.out.println(item.values());

            for (Double i: item.values()){
                clickCount.add(i);
            }


        }
        String clickCountString = clickCount.toString();


        return clickCountString;
    }

    @Override
    public String getBounceCount(CloudWatchClient cloudWatchClient) {
        Instant start = Instant.now().truncatedTo(ChronoUnit.MILLIS).minusSeconds(86400);
        Instant end = Instant.now().truncatedTo(ChronoUnit.MILLIS);

        Dimension dimension = Dimension.builder()
                .name("BulkEmail")
                .value("status")
                .build();


        Metric met = Metric.builder()
                .dimensions(dimension)
                .metricName("Hard bounce")
                .namespace("AWS/SES")
                .build();

        MetricStat metStat = MetricStat.builder()
                .stat("Sum")
                .period(86400)
                .metric(met)
                .build();

        MetricDataQuery dataQuery = MetricDataQuery.builder()
                .metricStat(metStat)
                .id("q1")
                .returnData(true)
                .build();

        List<MetricDataQuery> dq = new ArrayList<>();
        dq.add(dataQuery);

        GetMetricDataRequest getMetricDataRequest = GetMetricDataRequest.builder()
                .maxDatapoints(100)
                .startTime(start)
                .endTime(end)
                .metricDataQueries(dq)
                .build();

        GetMetricDataResponse response = cloudWatchClient.getMetricData(getMetricDataRequest);

        List<MetricDataResult> data = response.metricDataResults();


        List<Double>bounceCount = new ArrayList<>();

        for(MetricDataResult item: data){
            //label.add(item.label())
            System.out.println("Label:"+item.label());
            System.out.println("id:"+item.id());
            System.out.println("status:"+item.statusCodeAsString());
            System.out.println(item.values());

            for (Double i: item.values()){
                bounceCount.add(i);
            }


        }
        String bounceCountString = bounceCount.toString();


        return bounceCountString;
    }

    @Override
    public String getSpamMarkCount(CloudWatchClient cloudWatchClient) {
        Instant start = Instant.now().truncatedTo(ChronoUnit.MILLIS).minusSeconds(86400);
        Instant end = Instant.now().truncatedTo(ChronoUnit.MILLIS);

        Dimension dimension = Dimension.builder()
                .name("BulkEmail")
                .value("status")
                .build();


        Metric met = Metric.builder()
                .dimensions(dimension)
                .metricName("Complaint")
                .namespace("AWS/SES")
                .build();

        MetricStat metStat = MetricStat.builder()
                .stat("Sum")
                .period(86400)
                .metric(met)
                .build();

        MetricDataQuery dataQuery = MetricDataQuery.builder()
                .metricStat(metStat)
                .id("q1")
                .returnData(true)
                .build();

        List<MetricDataQuery> dq = new ArrayList<>();
        dq.add(dataQuery);

        GetMetricDataRequest getMetricDataRequest = GetMetricDataRequest.builder()
                .maxDatapoints(100)
                .startTime(start)
                .endTime(end)
                .metricDataQueries(dq)
                .build();

        GetMetricDataResponse response = cloudWatchClient.getMetricData(getMetricDataRequest);

        List<MetricDataResult> data = response.metricDataResults();


        List<Double>spamMarkCount = new ArrayList<>();

        for(MetricDataResult item: data){
            //label.add(item.label())
            System.out.println("Label:"+item.label());
            System.out.println("id:"+item.id());
            System.out.println("status:"+item.statusCodeAsString());
            System.out.println(item.values());

            for (Double i: item.values()){
                spamMarkCount.add(i);
            }


        }
        String spamMarkCountString = spamMarkCount.toString();


        return spamMarkCountString;
    }

    @Override
    public String getUnsubscribeCount(CloudWatchClient cloudWatchClient) {
        Instant start = Instant.now().truncatedTo(ChronoUnit.MILLIS).minusSeconds(86400);
        Instant end = Instant.now().truncatedTo(ChronoUnit.MILLIS);

        Dimension dimension = Dimension.builder()
                .name("BulkEmail")
                .value("status")
                .build();


        Metric met = Metric.builder()
                .dimensions(dimension)
                .metricName("Subscription")
                .namespace("AWS/SES")
                .build();

        MetricStat metStat = MetricStat.builder()
                .stat("Sum")
                .period(86400)
                .metric(met)
                .build();

        MetricDataQuery dataQuery = MetricDataQuery.builder()
                .metricStat(metStat)
                .id("q1")
                .returnData(true)
                .build();

        List<MetricDataQuery> dq = new ArrayList<>();
        dq.add(dataQuery);

        GetMetricDataRequest getMetricDataRequest = GetMetricDataRequest.builder()
                .maxDatapoints(100)
                .startTime(start)
                .endTime(end)
                .metricDataQueries(dq)
                .build();

        GetMetricDataResponse response = cloudWatchClient.getMetricData(getMetricDataRequest);

        List<MetricDataResult> data = response.metricDataResults();


        List<Double>unsubscribeCount = new ArrayList<>();

        for(MetricDataResult item: data){
            //label.add(item.label())
            for (Double i: item.values()){
                unsubscribeCount.add(i);
            }


        }
        String unsubscribeCountString = unsubscribeCount.toString();


        return unsubscribeCountString;
    }

    @Override
    public String getRenderingFailureCount(CloudWatchClient cloudWatchClient) {
        Instant start = Instant.now().truncatedTo(ChronoUnit.MILLIS).minusSeconds(86400);
        Instant end = Instant.now().truncatedTo(ChronoUnit.MILLIS);

        Dimension dimension = Dimension.builder()
                .name("BulkEmail")
                .value("status")
                .build();


        Metric met = Metric.builder()
                .dimensions(dimension)
                .metricName("Rendering failure")
                .namespace("AWS/SES")
                .build();

        MetricStat metStat = MetricStat.builder()
                .stat("Sum")
                .period(86400)
                .metric(met)
                .build();

        MetricDataQuery dataQuery = MetricDataQuery.builder()
                .metricStat(metStat)
                .id("q1")
                .returnData(true)
                .build();

        List<MetricDataQuery> dq = new ArrayList<>();
        dq.add(dataQuery);

        GetMetricDataRequest getMetricDataRequest = GetMetricDataRequest.builder()
                .maxDatapoints(100)
                .startTime(start)
                .endTime(end)
                .metricDataQueries(dq)
                .build();

        GetMetricDataResponse response = cloudWatchClient.getMetricData(getMetricDataRequest);

        List<MetricDataResult> data = response.metricDataResults();


        List<Double>renderingFailureCount = new ArrayList<>();

        for(MetricDataResult item: data){
            //label.add(item.label())
            for (Double i: item.values()){
                renderingFailureCount.add(i);
            }


        }
        String renderingFailureCountString = renderingFailureCount.toString();


        return renderingFailureCountString;
    }
}

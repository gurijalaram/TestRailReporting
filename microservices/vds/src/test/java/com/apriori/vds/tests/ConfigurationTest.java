package com.apriori.vds.tests;

import com.apriori.utils.TestRail;
import com.apriori.utils.http2.builder.common.entity.RequestEntity;
import com.apriori.utils.http2.builder.service.HTTP2Request;
import com.apriori.vds.entity.enums.VDSAPIEnum;
import com.apriori.vds.entity.response.configuration.Configuration;
import com.apriori.vds.entity.response.configuration.ConfigurationsItems;
import com.apriori.vds.tests.util.VDSRequestEntityUtil;
import com.apriori.vds.tests.util.VDSTestUtil;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Collections;
import java.util.HashMap;

public class ConfigurationTest extends VDSTestUtil {

    @Test
    @TestRail(testCaseId = {"7929"})
    @Description("Returns a list of CustomerConfigurations for a customer.")
    public void getConfigurations() {
        RequestEntity requestEntity = VDSRequestEntityUtil.initWithSharedSecret(VDSAPIEnum.GET_CONFIGURATIONS, ConfigurationsItems.class);

        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK,
            HTTP2Request.build(requestEntity).get().getStatusCode()
        );
    }

    @Test
    @TestRail(testCaseId = {"7930"})
    @Description("Get a specific CustomerConfiguration.")
    public void getConfigurationsByIdentity() {
        RequestEntity requestEntity = VDSRequestEntityUtil.initWithSharedSecret(VDSAPIEnum.GET_CONFIGURATIONS_BY_IDENTITY, Configuration.class)
            .inlineVariables(Collections.singletonList(""));

        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK,
            HTTP2Request.build(requestEntity).get().getStatusCode()
        );
    }

    @Test
    @TestRail(testCaseId = {"7931"})
    @Description("Replaces a CustomerConfiguration for a customer. Creates it if it is missing.")
    @Ignore
    public void putConfiguration() {
        RequestEntity requestEntity = VDSRequestEntityUtil.initWithSharedSecret(VDSAPIEnum.PUT_CONFIGURATION, null)
            .headers(new HashMap<String, String>(){{
                put("Content-Type", "application/json");
            }})
            .customBody(
                "{    'customerConfiguration': {        'createdBy': '#ETL00000000',        'configurationType': 'GROUP_DEFINITION',        'serializationType': 'COMPRESSED_XML',        'content': 'eJzlXNtu47YWfS/QfxD8WtARKYoXINMiSHLmBJ1JglzmNeBNGbW2FUhyOvn70texZdkWdQtyDpCH2CIp7sXFvRc3SZ/+8WM88l5NmsXJ5NMADv2BZyYq0fHk+dPg8eE/gA3++P3XX06f02T6cjXR5ocnbtM4SeOv4q8k/bauyYdwsH4UTwqPNh+KLDfpnXmNF48Z5cS+VSfq4e3FfBr8fNPAS+RfV9q2MPDUSGTZp4FKxsNIqqEWuRgn2oyGmVHTNM7fhp9/1rMd9rxTkedpLKe5yVbtoHU7f4lXMZzm8Wj4kBrzVbwMvJNFrfnr1zWCAzXm5W0NM8nTt+UH+/Fv87aqjXf6PW9AJakZZt9FavQwtzZnbxaR8XBmvv7TvA3WbdnWJmJsvFcxmlpkECKaIC6AH2oNcKQIYDBCgPCQsMD+GchWdiyrz5q/3mhi1pyeI7VZ8PTEdvrnp3nZlQ1hZey3+r0G/9ussTWe5PgILBvQJlNp/JJbjqw6//DdeNpEYjrKPaHH8STO8lTkSZp5z0WTbAuxXlWEhSdjM5aWnd/jl1WJr2fXj2dfCsUmyWNmi63K5OnUFEq82DGc5HPrvTi7no5G5cVMOo6zGd3XQNDjxFpW3qbXBsk264/E5Hl4byGfPA/WVsNQCS0YIJIzgKExQDDuA0k4IQEx9hu63dHd0WeVR/92beOg0OJqUnmpiZaMKr50CyM7BRqacDKvvg3jyS6O9aFlmIvIVyGAjGCAJQuAwD4FCisaMUIVNdExaPn7QlvThO6hVXaoJZZAIsMB9rECPDLU+rwwIL6WRIfmGLTQf2ds69nQObYaEUkho0BQigAWXANOfQiMkQpDEfiR0UexrR6MO8G2pg3dY0s1VBELQRTSEGCuCZCG2klFpJBhJEKM+FFs0TtjW8+GStienmxEwa3gmCavZiImyhwKoan4Z1PHbIf/QtlsKj9vqThYJuP+K7Lv9yYvRNsFdKt6uwKuiviZN7RHAMGwqgKat7Khgn6f659kmoMkArn9VyY/FrrH+yfOv3uTZAIskmkaayFHxtuAe+hd5RZcT3gWmqWFkTdro1RGnZ5svrbQo72qav60mrKaFz2qrhbs2FBY+wldprDgIa25OyHKJv78QZXJj5CiPtE+8H1pJw4zDDASSEAQ4khopQUqnYY7DoC24gA2mLzEbMbkstfv8wQNDCrxBovvyxFuBjzXWpkAauuisFUxOOJA+BoDYqViiKRvGCwRiCXAtyN0mwPfwKBegVccQ25wBDibhWFkQiA5MiBiCAWMSCK4rAR8OzK4OfANDOoVeM00RJwjK9Q5sbo9sPrHRCGwtJERl34QiJIYvQs8akcjNwe+gUG9Am+olCHXAfBhBK044gZwn9kFaUCQxBAHPkeVgG9HQDcHvoFBTsDvU36LnlVRf/OSBQWYTW2rT1MrIUrK7ihAVJb6WynA3fqztNmfP/N4KCgRDJuZOYMMVtK6jFAbDTBEPhChUICGIWHSF5zxkkVJ5fzcCsdltwpKe06CLaW9Nr+YCpzZtGTLFlm2B3R7GA/kN1HLCc4QhlgwLgEMqbQwMh8wFgSAKuwbClHEi2u7FhKcqN0MJ2qU4jzzshejYjFaKnsxGnnTuUy2YHpimidjkcfKfv3mxRM1mlq+2X8+UuYT9ZD6RJhxpDkGigcc4IhSwCKsAMGMCBpwjRgrmWDbvOgi91mumvdo8HpGdJ7pIBQHShEKuODCBmziA0mhACHmkYwEJFa+HgW3i+ynC7g1jegcXCqs9AykBGymgTBUAljFz4BvRxxHiEkeHWVu0EX60wXcmkZ0Di4nAkXCR4AhZhdXnCAgCSUgkr6EVvWYICRHwe0i/+kCbk0jOgfX+MrXIlLWU2EbvKkdbw7t8jWgBtouSczU0e2moIsEqAu4NY3oHlzCfEl4aKWQHW9MIQbSkABE1AgoSAQjrY6CG7w3uPWMeI/08mg0X1oczSwHuOq6oqi/URsCPNjVro0EuEShXcZQDqiW1DpuGtp1jC8B9UNEIztCmheRay7AA9KqAA8O6UunMwavL3sS5B9IbQese7WNiVKUIQoggTOhGtpwj6kBggqfByqKdOgfdU5dCMKgJBDuc041jeje8ytIrFzSdiLOtJREBLBAUGCEgtSH1Pb6qNrGXQhCF3BrGvEOnt/O+Se3zUUM64aAIGwSAlZxJNhqZDHuJRnByjEF72qwZqfWMIGIKg1MaCDA2kgg/SCyotUIo7BPdDHq78SUTH03Y+EUVHB1sVMlqOCyOH88qBzgnY0aB55uRI4DpQqBIxKjrL3IgSvvVNebaRfLKPvt9vL4HCtLqlWaYxg1mWOr1+9uyzaaEirQRFnXBygUNtYgzKxj5NwKYcS0CLUPWdTBlKie0Ko0Jfj/3ZQI/X6mxL2yVdM4OTovwtqxB9Nq82K5vbAIrKWYHDpUfWBqtX16tXBquZL7Lxc7ZcsCcbtReHieGpGXsHfPMuDvJgdaLaPTsRhdmMnbgQXCotDnVEzyA6Us3S7i7GUk3rYWu8dtS02WTFO1rnJ7eff16v7+6ua6WHA6Mgc68GpSuRjnVaHzu8uzh8smPrrtszkFIlU6AuZEpPkUHj5ezDqaTJ7ng1aHS+5ndGpzqcyN7iVTBfuKfPp8d/N425RKj7cXlkq/XVx+ueyYUY4bMwVGVdpvdGKUlVDD8yTLH7N5N2tQyXmrqRe3dMiuIoVs2ca+6Ob+4enx/ur6c5fscU00FNhTKVnmzJ7Hizq0cc+Z9EabHYM64EtvDsd1s7JAmUqHSp0oc56MX5KJtWR4V4857tuvvTDnkF1FAp3ffL29ub68fni6P7+8Pru7umnKJyuFLn7rjVSum7QFUlVaTNYkVX197b7z3DOvKsnr9qnVvcx2vU9WoFOl9H/d9Vo9H+V+Q67vtdpRH9XeOq03t+R6d26bR6TSTket5VotCjlfBOyFQntM+tirMtdzMQXiVDrW40Scu2Q0sjDXD2buJ316Yc8hu4oUurv58qU5h7qPXq7XbgrkqXRsqX70apopcr9U1E+mqKqR/wNhzfV6UYFgPaSzm5LM/QJV7yQ7auhHznO7XowvMKz9PPcG8GdZlqi4Zhh0v/Hftw7fZ153dDq7uHh6uHnaUWitk8rxFwEKpGo/1b2QrvU1lftPHPSoyCtJqlZUeQ/uyPFaZ4E57ae5NyFuGuvc76z2ufXmHOY+CKVcD+cXKNV+Gny5EqqZA3e/a9Dn8u54ArydtV2/SW/XKwgFCrWf9J7tVd0ZURQw1QjkfJ+it+23EpM62ICbcadTtjjekC+wpdOc9lpzNo1l7j8D0Pu6rYqtH19rux6S32YbbT/zPT9+UVtpu5/57+9MSRWd3caBEgdJtHuN4XR2A81G459HbmnZcc8WzlpemCx+nhzg1qH85uOyl4M9PChp/AAIa5vt5+XZ0/lv+tqPv/7yLw+9gDg='    }}"
            );

        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_CREATED,
            HTTP2Request.build(requestEntity).post().getStatusCode()
        );
    }
}


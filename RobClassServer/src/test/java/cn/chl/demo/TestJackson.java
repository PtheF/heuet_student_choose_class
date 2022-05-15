package cn.chl.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

public class TestJackson {

    private final ObjectMapper JSON = new ObjectMapper();

    @Test
    public void test() throws Exception{

        String count = "100";

        Integer integer = JSON.readValue(count, Integer.class);

        System.out.println(integer);

    }
}

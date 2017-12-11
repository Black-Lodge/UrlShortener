package urlshortener.blacklodge.services;

import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static java.lang.Math.pow;

@Service
public class HashGeneratorServiceImpl implements HashGeneratorService {
    private static HashFunction function = Hashing.murmur3_32();

    public List<Integer> hash (String url) {
        Long num = function.hashString(url, StandardCharsets.UTF_8).asLong();
        int div = (int)pow(10,String.valueOf(num).length()/2);
        return new LinkedList<>(Arrays.asList((int)(num/div),(int)(num%div)));
    }
}

package com.LTTBDD.ecommerce_app.common.utils;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

public class StringUtils {

	public static String generateTransactionId() {
		String uuid = UUID.randomUUID().toString();
		System.out.println("uuid: " + uuid);
		return uuid;
	}
	
	public static String generateRandomNumber() {
		
		Set<Long> generatedNumbers = new HashSet<>();
        Random random = new Random();
        while (true) {
            Long randomNumber = random.nextLong() % 10000000000L;
            if (randomNumber < 0) {
                randomNumber *= -1;
            }
            if (!generatedNumbers.contains(randomNumber)) {
                generatedNumbers.add(randomNumber);
                return Long.toString(randomNumber);
            }
        }
        
    }
}

package com.kenazalfizza.farmersmarketplace.generator;

import java.util.Random;

public class IdGenerator {

    public String userId(String name) {
        StringBuilder id = new StringBuilder();
        String id_sub = "us";

        int id_length = 12;
        int id_num = 0;
        Random rand = new Random();

        for (int i=0; i<id_length; i++) {
            id_num = rand.nextInt(9);
            id.append(id_num);
        }

        return id_sub + id;
    }

    public String storeId() {
        StringBuilder id = new StringBuilder();
        String id_sub = "st";

        int id_length = 8;
        int id_num = 0;
        Random rand = new Random();

        for (int i=0; i<id_length; i++) {
            id_num = rand.nextInt(9);
            id.append(id_num);
        }

        return id_sub + id;
    }

    public String productId() {
        StringBuilder id = new StringBuilder();
        String id_sub = "pr";

        int id_length = 12;
        int id_num = 0;
        Random rand = new Random();

        for (int i=0; i<id_length; i++) {
            id_num = rand.nextInt(9);
            id.append(id_num);
        }

        return id_sub + id;
    }
}

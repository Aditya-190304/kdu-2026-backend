package com.example.company.warehouse;

import java.util.*;
import java.util.stream.Collectors;

public class Inventory {
    private String name;
    private List<List<String>> palletItemIds;

    public Inventory(String name, List<List<String>> palletItemIds) {
        this.name = name;
        this.palletItemIds = palletItemIds;
    }

    public List<List<String>> getPalletItemIds() {
        return palletItemIds;
    }


    public static Inventory findItem(String id) {
        if (id.equals("A100")) {
            return new Inventory("Main Inventory", List.of(
                    List.of("P10", "P20"),
                    List.of("P30", "P10", "P40")
            ));
        }
        return null;
    }

    public static void main(String[] args) {


        Object item = Optional.ofNullable(Inventory.findItem("A100"))
                .<Object>map(inv -> inv)
                .orElseGet(ItemPlaceholder::new);


        if (item instanceof Inventory) {
            Set<String> uniqueIds = ((Inventory) item)
                    .getPalletItemIds()
                    .stream()
                    .flatMap(List::stream)
                    .collect(Collectors.toSet());

            System.out.println(uniqueIds);
        }
    }
}


class ItemPlaceholder {
    public ItemPlaceholder() {
        System.out.println("ALERT: Creating expensive placeholder object!");
    }

    public String getInfo() {
        return "ID-NOT-FOUND: Placeholder Item";
    }
}

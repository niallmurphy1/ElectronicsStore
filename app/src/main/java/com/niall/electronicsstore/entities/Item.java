package com.niall.electronicsstore.entities;

import java.util.ArrayList;
import java.util.Comparator;

public class Item {

    private String id;
    private String name;
    private String manufacturer;
    private int priceCents;
    private String category;
    private String image;
    private double rating;
    private ArrayList<String> reviews;
    private int stockLevel;
    private String description;


    public static Comparator<Item> itemComparatorAZName = new Comparator<Item>() {
        @Override
        public int compare(Item item1, Item item2) {
            return item1.getName().compareTo(item2.getName());
        }
    };

    public static Comparator<Item> itemComparatorZAName = new Comparator<Item>() {
        @Override
        public int compare(Item item1, Item item2) {
            return item2.getName().compareTo(item1.getName());
        }
    };

    public static Comparator<Item> itemComparatorPriceLowHigh = new Comparator<Item>() {
        @Override
        public int compare(Item item1, Item item2) {
            return (int) (item1.getPriceCents() - item2.getPriceCents());
        }
    };

    public static Comparator<Item> itemComparatorPriceHighLow = new Comparator<Item>() {
        @Override
        public int compare(Item item1, Item item2) {
            return (int) (item2.getPriceCents() - item1.getPriceCents());
        }
    };


    public static Comparator<Item> itemComparatorManufacturer = new Comparator<Item>() {
          @Override
          public int compare(Item item1, Item item2) {
              return item1.getManufacturer().compareTo(item2.getManufacturer());
          }
      };

      public Item(String id, String name, String manufacturer, int priceCents, String category, String image, double rating, ArrayList<String> reviews, int stockLevel, String description) {
        this.id = id;
        this.name = name;
        this.manufacturer = manufacturer;
        this.priceCents = priceCents;
        this.category = category;
        this.image = image;
        this.rating = rating;
        this.reviews = reviews;
        this.stockLevel = stockLevel;
        this.description = description;
    }

    public Item(String name, String manufacturer, int priceCents, String category, String image){
        this.name = name;
        this.manufacturer = manufacturer;
        this.priceCents = priceCents;
        this.category = category;
        this.image = image;
    }



    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public ArrayList<String> getReviews() {
        return reviews;
    }

    public void setReviews(ArrayList<String> reviews) {
        this.reviews = reviews;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public int getPriceCents() {
        return priceCents;
    }

    public void setPriceCents(int priceCents) {
        this.priceCents = priceCents;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getStockLevel() {
        return stockLevel;
    }

    public void setStockLevel(int stockLevel) {
        this.stockLevel = stockLevel;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Item{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", manufacturer='" + manufacturer + '\'' +
                ", price=" + priceCents +
                ", category='" + category + '\'' +
                ", image='" + image + '\'' +
                ", rating=" + rating +
                ", reviews=" + reviews +
                ", stockLevel=" + stockLevel +
                '}';
    }
}

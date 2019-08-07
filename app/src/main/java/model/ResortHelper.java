package model;

public class ResortHelper {
    String title;
    String price;
    String resort;

    public ResortHelper(){

    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getResort() {
        return resort;
    }

    public void setResort(String resort) {
        this.resort = resort;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    String image_url;

    public ResortHelper(String title, String price, String resort, String image_url) {
        this.title = title;
        this.price = price;
        this.resort = resort;
        this.image_url = image_url;
    }



}

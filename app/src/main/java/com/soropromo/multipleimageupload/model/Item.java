package com.soropromo.multipleimageupload.model;

public class Item {
    private String imageUri;
    private String imageName;
    private String uploaded;

    public Item() {
    }

    public Item(String imageUri, String imageName, String uploaded) {
        this.imageUri = imageUri;
        this.imageName = imageName;
        this.uploaded = uploaded;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getUploaded() {
        return uploaded;
    }

    public void setUploaded(String uploaded) {
        this.uploaded = uploaded;
    }
}

package com.snoussi.univox;

public class EmergencyItem {
    private String itemName;
    private int itemIcon;

    public EmergencyItem(String itemName, int itemIcon) {
        this.itemName = itemName;
        this.itemIcon = itemIcon;
    }

    public EmergencyItem() {
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getItemIcon() {
        return itemIcon;
    }

    public void setItemIcon(int itemIcon) {
        this.itemIcon = itemIcon;
    }
}

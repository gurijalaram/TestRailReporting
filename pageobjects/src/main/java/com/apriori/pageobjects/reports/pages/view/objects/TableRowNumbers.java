package com.apriori.pageobjects.reports.pages.view.objects;

import java.math.BigDecimal;

public class TableRowNumbers {

    private int quantity;
    private BigDecimal cycleTime;
    private BigDecimal piecePartCost;
    private BigDecimal fullyBurdenedCost;
    private BigDecimal capitalInvestments;

    public int getQuantity() {
        return quantity;
    }

    public BigDecimal getCycleTime() {
        return cycleTime;
    }

    public BigDecimal getPiecePartCost() {
        return piecePartCost;
    }

    public BigDecimal getFullyBurdenedCost() {
        return fullyBurdenedCost;
    }

    public BigDecimal getCapitalInvestments() {
        return capitalInvestments;
    }

    public void setQuantity(int quantity) {
        this.quantity =  quantity;
    }

    public void setCycleTime(BigDecimal cycleTime) {
        this.cycleTime = cycleTime;
    }

    public void setPiecePartCost(BigDecimal piecePartCost) {
        this.piecePartCost = piecePartCost;
    }

    public void setFullyBurdenedCost(BigDecimal fullyBurdenedCost) {
        this.fullyBurdenedCost = fullyBurdenedCost;
    }

    public void setCapitalInvestments(BigDecimal capitalInvestments) {
        this.capitalInvestments = capitalInvestments;
    }

}

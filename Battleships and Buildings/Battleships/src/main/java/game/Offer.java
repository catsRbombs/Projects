// Create a new file: game/Offer.java

package game;

import java.io.Serializable;

public class Offer implements Serializable {
    private String sender;
    private String recipient;

    private int offerMoney;
    private int offerSteel;
    private int offerCannon;
    private int offerDoubleCannon;
    private int offerRedBeardCannon;
    private int offerBomb;

    private int requestMoney;
    private int requestSteel;
    private int requestCannon;
    private int requestDoubleCannon;
    private int requestRedBeardCannon;
    private int requestBomb;

    // Constructor
    public Offer(String sender, String recipient,
                 int offerMoney, int offerSteel, int offerCannon, int offerDoubleCannon, int offerRedBeardCannon, int offerBomb,
                 int requestMoney, int requestSteel, int requestCannon, int requestDoubleCannon, int requestRedBeardCannon, int requestBomb) {
        this.sender = sender;
        this.recipient = recipient;

        this.offerMoney = offerMoney;
        this.offerSteel = offerSteel;
        this.offerCannon = offerCannon;
        this.offerDoubleCannon = offerDoubleCannon;
        this.offerRedBeardCannon = offerRedBeardCannon;
        this.offerBomb = offerBomb;

        this.requestMoney = requestMoney;
        this.requestSteel = requestSteel;
        this.requestCannon = requestCannon;
        this.requestDoubleCannon = requestDoubleCannon;
        this.requestRedBeardCannon = requestRedBeardCannon;
        this.requestBomb = requestBomb;
    }

    // Getters and setters

    public String getSender() { return sender; }
    public String getRecipient() { return recipient; }

    public int getOfferMoney() { return offerMoney; }
    public int getOfferSteel() { return offerSteel; }
    public int getOfferCannon() { return offerCannon; }
    public int getOfferDoubleCannon() { return offerDoubleCannon; }
    public int getOfferRedBeardCannon() { return offerRedBeardCannon; }
    public int getOfferBomb() { return offerBomb; }

    public int getRequestMoney() { return requestMoney; }
    public int getRequestSteel() { return requestSteel; }
    public int getRequestCannon() { return requestCannon; }
    public int getRequestDoubleCannon() { return requestDoubleCannon; }
    public int getRequestRedBeardCannon() { return requestRedBeardCannon; }
    public int getRequestBomb() { return requestBomb; }
}

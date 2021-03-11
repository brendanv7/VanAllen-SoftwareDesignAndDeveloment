/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package concurrency.actor;

/**
 *
 * @author roncoleman125
 */
public class Ack {
    private final int id;
    
    public Ack(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
    
    @Override
    public String toString() {
        return "ack id = "+getId();
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package concurrency.actor;

import com.googlecode.actorom.Address;
import java.io.Serializable;

/**
 *
 * @author roncoleman125
 */
public class Good implements Serializable {
    private static int serialno = 0;
    
    private final int id;
    private final Address source;
    
    public Good(Address source) {
        this.id = serialno++;
        this.source = source;
    }

    public int getId() {
        return id;
    }

    public Address getSource() {
        return source;
    }
}

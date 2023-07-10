package edu.coursera.concurrent;

import edu.rice.pcdp.Actor;
import edu.rice.pcdp.PCDP;

import java.util.ArrayList;
import java.util.List;

/**
 * An actor-based implementation of the Sieve of Eratosthenes.
 *
 * TODO Fill in the empty SieveActorActor actor class below and use it from
 * countPrimes to determine the number of primes <= limit.
 */
public final class SieveActor extends Sieve {

    /**
     * {@inheritDoc}
     *
     * TODO Use the SieveActorActor class to calculate the number of primes <=
     * limit in parallel. You might consider how you can model the Sieve of
     * Eratosthenes as a pipeline of actors, each corresponding to a single
     * prime number.
     */
    @Override
    public int countPrimes(final int limit) {
        SieveActorActor sieveActorActor = new SieveActorActor(2);
        PCDP.finish(()->{
            for (int i =3;i<=limit;i+=2){
                sieveActorActor.send(i);
            }
            sieveActorActor.send(0);
        });
        int count=1;
        SieveActorActor localSieveActorActor = sieveActorActor;
        while (localSieveActorActor.next!=null){
            count++;
            localSieveActorActor=localSieveActorActor.next;
        }
        return count;
    }

    /**
     * An actor class that helps implement the Sieve of Eratosthenes in
     * parallel.
     */
    public static final class SieveActorActor extends Actor {
        private final int localPrime;
        private SieveActorActor next;

        public SieveActorActor(final int initPrime ) {
            this.localPrime = initPrime;
            this.next=null;
        }

        /**
         * Process a single message sent to this actor.
         *
         * TODO complete this method.
         *
         * @param msg Received message
         */
        @Override
        public void process(final Object msg) {
            int candidate = (Integer) msg;
            if (candidate<=0){
                if (this.next!=null){
                    this.next.send(candidate);
                }
            }else {
                boolean nonMulOfLocalPrime = (candidate%localPrime)!=0;
                if (nonMulOfLocalPrime){
                    if (this.next==null){
                        this.next=new SieveActorActor(candidate);
                    }else {
                        this.next.send(candidate);
                    }
                }
            }

        }

    }
}

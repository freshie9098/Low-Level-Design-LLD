public class LeakyBucket{
    private final int capacity;
    private int refillTokenRatePerMinute;
    private int size ;
    private long lastRequestTime;
    
    public LeakyBucket(int capacity,int refillTokenRatePerMinute){
        this.capacity = capacity;
        this.refillTokenRatePerMinute = refillTokenRatePerMinute;
        size = 0;
        lastRequestTime = -1;
    }
    
    public boolean isRequestAllowed(){
        int currTime = Sytem.currentTimeMillis();
        if(lastRequestTime == -1){
            if(size < capacity){
            size+=1;
            lastRequestTime = currTime;
            return true;
                 }
            else{
                false;
            }
        }
        else{
        int sizeBeforeResize = this.size;
        int decreaseInSize = (currTime-lastRequestTime)*refillTokenRatePerMinute/60000;
        size = Math.max(sizeBeforeResize - decreaseInSize,0);
        if(size < capacity){
            size+=1;
            lastRequestTime = currTime;
            return true;
        }
        return false;
        }
    }   
    
    
}

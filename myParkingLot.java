//VEHICLE

public enum VehicleType{
    CAR,
    BIKE,
    TRUCK
}

public abstract class Vehicle{
    private String vehicleId;
    private vehicleType vehicleType;
    
    public Vehicle(String vehicleId,vehicleType vehicleType){
        this.vehicleId = vehicleId;
        this.vehicleType = vehicleType;
    }   
    
    public String getVehicleId(){
        return vehicleId;
    }
    public vehicleType getVehicleType(){
        return vehicleType;
    }
    
    public abstract int getSpaceRequired();
} 

public class Car extends vehicle{
    public Car(String vehicleId){
        super(vehicleId,vehicleType.CAR);
    }
    public int getSpaceRequired(){
        return 1;
    }
}
//spot
public enum SpotType{
    CAR,TRUCK,BIKE
}

public class ParkingSpot{
   
    private final String spotId;
    SpotType spotType;
    private boolean isAvailable = true;
    private Vehicle parkedVehicle;
    
    public ParkingSpot(String spotId,SpotType spotType){
        this.spotId = spotId;
        this.spotType = spotType;
    }
    
    public String getSpotId(){
        return spotId;
    }
    
    public SpotType getspotType(){
        return spotType;
    }
    
    public Vehicle getParkedVehicle(){
        return parkedVehicle;
    }
    
    public synchronized boolean getisAvailable(){
        return isAvailable;
    }
    
    public synchronized void parkVehicle(Vehicle vehicle){
        if(!isAvailable || !canFitVehicle)return;
        this.parkedVehicle = vehicle;
        this.isAvailable = false;
    }
    public synchronized void unParkVehicle(){
        this.parkedVehicle = null;
        this.isAvailable = true;
    }
    
    public boolean canFitVehicle(Vehicle vehicle){
        return switch (vehicle.getVehicleType()) {
            case CAR->spotType == SpotType.CAR;
            case BIKE->spotType == SpotType.BIKE;
            case TRUCK->spotType == SpotType.TRUCK;
        }
    }
    
}


//FLOOR
public class Floor{
    private  final Map<String,ParkingSpot>spots;
    private String floorId;
    
    
    //
    public Floor(String floorId){
        this.floorId = floorId;
        this.spots = new ConcurrentHashMap<>();
    }
    //getter setter
    
    //addSpot
    public void addSpot(ParkingSpot spot){
        spots.put(spot.getSpotId(),spot);
    }
    
    //findAvailableSpot
    public ParkingSpot findAvailableSpot(Vehicle vehicle){
        for(ParkingSpot s:spots.values()){
                if(s.isAvailable() && s.canFitVehicle(vehicle)){
                    return s;
                }
            }
        return null;
    }
    
    
}

//TICKET
public class Ticket{
    private final String ticketId;
    private final LocalDateTime entryTime;
    private LocalDateTime exitTime;
    private final Vehicle vehicle;
    private ParkingSpot Spot;
    
    public Ticket(String ticketId,ParkingSpot Spot,Vehicle vehicle){
        this.ticketId = ticketId;
        this.vehicle = vehicle;
        this.Spot = Spot;
        this.entryTime = LocalDateTime.now();
    }
    //markExit
    public void markExit(){
        exitTime = LocalDateTime.now();
    }
    //getDurationTime
    public long getDurationTimeInHours(){
        if(exitTime == null)return 0;
        return Math.max(1,Duration.between(entryTime,exitTime).toHours());
    }
    //getters
    
}
//PRICING STRATEGY
public interface PricingStrategy{
    public double CalculateFee(Ticket ticket);
}

class CarPricingStrategy implements PricingStrategy{
    @Override
    double CalculateFee(Ticket ticket){
        return 100*(ticket.getDurationTimeInHours());
    }
}


class BikePricingStrategy implements PricingStrategy{
    @Override
    double CalculateFee(Ticket ticket){
        return 200*(ticket.getDurationTimeInHours());
    }
}


class TruckPricingStrategy implements PricingStrategy{
    @Override
    double CalculateFee(Ticket ticket){
        return 300*(ticket.getDurationTimeInHours());
    }
}

//PricingStrategyFactory
class PricingStrategyFactory{
    public static getPricingStrategy(VehicleType type){
       return switch(type){
           case CAR -> new CarPricingStrategy();
           case BIKE -> new BikePricingStrategy();
           case TRUCK -> new TruckPricingStrategy();
       }
    }
}


//Payment
public interface Payment{
    void Pay(double amount); 
}

class UPIPayment implements Payment{
    public void Pay(double amount){
        System.out.println("UPI Payment done for amount :"+amount);
    }
}
class CardPayment implements Payment{
    public void Pay(double amount){
        System.out.println("Card Payment done for amount :"+amount);
    }
}

//parkingLot
public class ParkingLot{
    private static ParkingLot instance;
    private List<Floor>floors;
    private PricingStrategy pricingStrategy;
    
    
    private ParkingLot(){
        this.floors = new ArrayList<>();
    }
    
    public static synchronized getInstance(){
        if(instance == null){
            return new ParkingLot();
        }
        return instance;
    }
    //add floors
    //park Vehicle
    public Ticket parkVehicle(Vehicle v){
        //findAvailableSpot
        for(floor f:floors){
            ParkingSpot spot = f.findAvailableSpot(v);
            if(spot!=null){
                spot.parkVehicle(v);
                return new Ticket(UUID.randomUUID().toString(),spot,v);
            }
        }
        throw new Runtime Exception (
            "Parking Full"
            );
    }
    //unpark Vehicle
    public double unParkVehicle(Ticket ticket,PricingStrategy pricingStrategy){
        PricingStrategy PricingStrategyType= PricingStrategyFactory.getPricingStrategy(ticket.);
        double fees = PricingStrategyType.CalculateFee();
        
    }
    
    
    
}











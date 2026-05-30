import java.time.*;
import java.util.*;

// ===================== ENUMS =====================

enum VehicleType {
    CAR,
    BIKE,
    TRUCK
}

enum SpotType {
    CAR,
    BIKE,
    TRUCK
}

// ===================== VEHICLE =====================

class Vehicle {
    private String vehicleNumber;
    private VehicleType vehicleType;

    public Vehicle(String vehicleNumber,
                   VehicleType vehicleType) {
        this.vehicleNumber = vehicleNumber;
        this.vehicleType = vehicleType;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }
}

// ===================== PARKING SPOT =====================

class ParkingSpot {

    private int spotId;
    private SpotType spotType;
    private boolean occupied;

    private Vehicle vehicle;

    public ParkingSpot(int spotId,
                       SpotType spotType) {
        this.spotId = spotId;
        this.spotType = spotType;
    }

    public boolean isOccupied() {
        return occupied;
    }

    public int getSpotId() {
        return spotId;
    }

    public boolean canFitVehicle(Vehicle vehicle) {

        return switch (vehicle.getVehicleType()) {
            case CAR -> spotType == SpotType.CAR;
            case BIKE -> spotType == SpotType.BIKE;
            case TRUCK -> spotType == SpotType.TRUCK;
        };
    }

    public void parkVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
        occupied = true;
    }

    public void removeVehicle() {
        vehicle = null;
        occupied = false;
    }
}

// ===================== PARKING FLOOR =====================

class ParkingFloor {

    private int floorId;
    private List<ParkingSpot> spots;

    public ParkingFloor(int floorId) {
        this.floorId = floorId;
        this.spots = new ArrayList<>();
    }

    public void addSpot(ParkingSpot spot) {
        spots.add(spot);
    }

    public ParkingSpot findAvailableSpot(
            Vehicle vehicle) {

        for (ParkingSpot spot : spots) {

            if (!spot.isOccupied()
                    && spot.canFitVehicle(vehicle)) {
                return spot;
            }
        }

        return null;
    }
}

// ===================== TICKET =====================

class Ticket {

    private String ticketId;
    private Vehicle vehicle;
    private ParkingSpot parkingSpot;

    private LocalDateTime entryTime;
    private LocalDateTime exitTime;

    public Ticket(String ticketId,
                  Vehicle vehicle,
                  ParkingSpot parkingSpot) {

        this.ticketId = ticketId;
        this.vehicle = vehicle;
        this.parkingSpot = parkingSpot;

        this.entryTime = LocalDateTime.now();
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void markExit() {
        exitTime = LocalDateTime.now();
    }

    public long getDurationInHours() {

        if (exitTime == null) {
            return 0;
        }

        return Math.max(
                1,
                Duration.between(
                        entryTime,
                        exitTime
                ).toHours()
        );
    }

    public ParkingSpot getParkingSpot() {
        return parkingSpot;
    }
}

// ===================== PRICING STRATEGY =====================

interface PricingStrategy {

    double calculateFee(Ticket ticket);
}

class CarPricingStrategy
        implements PricingStrategy {

    public double calculateFee(Ticket ticket) {

        return ticket.getDurationInHours() * 20;
    }
}

class BikePricingStrategy
        implements PricingStrategy {

    public double calculateFee(Ticket ticket) {

        return ticket.getDurationInHours() * 10;
    }
}

class TruckPricingStrategy
        implements PricingStrategy {

    public double calculateFee(Ticket ticket) {

        return ticket.getDurationInHours() * 50;
    }
}

// ===================== FACTORY =====================

class PricingStrategyFactory {

    public static PricingStrategy getStrategy(
            VehicleType type) {

        return switch (type) {

            case CAR -> new CarPricingStrategy();
            case BIKE -> new BikePricingStrategy();
            case TRUCK -> new TruckPricingStrategy();
        };
    }
}

// ===================== PAYMENT STRATEGY =====================

interface PaymentStrategy {

    void pay(double amount);
}

class UPIPayment
        implements PaymentStrategy {

    public void pay(double amount) {

        System.out.println(
                "Paid ₹" + amount + " via UPI"
        );
    }
}

class CardPayment
        implements PaymentStrategy {

    public void pay(double amount) {

        System.out.println(
                "Paid ₹" + amount + " via Card"
        );
    }
}

// ===================== PARKING LOT =====================

class ParkingLot {

    private List<ParkingFloor> floors =
            new ArrayList<>();

    public void addFloor(
            ParkingFloor floor) {

        floors.add(floor);
    }

    public Ticket parkVehicle(
            Vehicle vehicle) {

        for (ParkingFloor floor : floors) {

            ParkingSpot spot =
                    floor.findAvailableSpot(
                            vehicle);

            if (spot != null) {

                spot.parkVehicle(vehicle);

                return new Ticket(
                        UUID.randomUUID()
                                .toString(),
                        vehicle,
                        spot
                );
            }
        }

        throw new RuntimeException(
                "Parking Full"
        );
    }

    public double unparkVehicle(
            Ticket ticket,
            PaymentStrategy paymentStrategy) {

        ticket.markExit();

        PricingStrategy pricingStrategy =
                PricingStrategyFactory
                        .getStrategy(
                                ticket
                                        .getVehicle()
                                        .getVehicleType()
                        );

        double fee =
                pricingStrategy
                        .calculateFee(ticket);

        paymentStrategy.pay(fee);

        ticket.getParkingSpot()
                .removeVehicle();

        return fee;
    }
}

// ===================== MAIN =====================

public class Main {

    public static void main(String[] args)
            throws Exception {

        ParkingLot parkingLot =
                new ParkingLot();

        ParkingFloor floor =
                new ParkingFloor(1);

        floor.addSpot(
                new ParkingSpot(
                        1,
                        SpotType.CAR
                ));

        floor.addSpot(
                new ParkingSpot(
                        2,
                        SpotType.BIKE
                ));

        parkingLot.addFloor(floor);

        Vehicle car =
                new Vehicle(
                        "WB01AB1234",
                        VehicleType.CAR
                );

        Ticket ticket =
                parkingLot.parkVehicle(car);

        Thread.sleep(2000);

        parkingLot.unparkVehicle(
                ticket,
                new UPIPayment()
        );
    }
}

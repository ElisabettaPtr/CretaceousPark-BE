package com.petraccia.elisabetta.CretaceousPark;

import com.petraccia.elisabetta.CretaceousPark.enums.AvailabilityStatus;
import com.petraccia.elisabetta.CretaceousPark.enums.DangerLevel;
import com.petraccia.elisabetta.CretaceousPark.model.*;
import com.petraccia.elisabetta.CretaceousPark.repository.*;
import com.petraccia.elisabetta.CretaceousPark.spring_jwt.model.AuthUser;
import com.petraccia.elisabetta.CretaceousPark.spring_jwt.model.ERole;
import com.petraccia.elisabetta.CretaceousPark.spring_jwt.model.Role;
import com.petraccia.elisabetta.CretaceousPark.spring_jwt.repository.AuthRoleRepository;
import com.petraccia.elisabetta.CretaceousPark.spring_jwt.repository.AuthUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Component
public class DataLoadRunner implements CommandLineRunner {

    @Autowired
    AuthUserRepository authUserRepository;

    @Autowired
    AuthRoleRepository authRoleRepository;

    @Autowired
    AdminRepository adminRepository;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    AttractionRepository attractionRepository;

    @Autowired
    BookableRepository bookableRepository;

    @Autowired
    BookingRepository bookingRepository;

    @Autowired
    DayTypeRepository dayTypeRepository;

    @Autowired
    NonBookableRepository nonBookableRepository;

    @Autowired
    PlannerRepository plannerRepository;

    @Autowired
    RestaurantRepository restaurantRepository;

    @Autowired
    ShowRepository showRepository;

    @Autowired
    TicketRepository ticketRepository;

    @Autowired
    TypeServiceRepository typeServiceRepository;

    @Autowired
    WalletRepository walletRepository;

    @Autowired
    ZoneRepository zoneRepository;

    @Autowired
    private PasswordEncoder encoder;

    public void run(String... args) throws Exception {

        // Creazione dei ruoli
        Role r_admin = new Role();
        r_admin.setName(ERole.ROLE_ADMIN);
        authRoleRepository.save(r_admin);

        Role r_customer = new Role();
        r_customer.setName(ERole.ROLE_CUSTOMER);
        authRoleRepository.save(r_customer);

        // Creazione degli AuthUser per gli admin
        AuthUser u_admin1 = new AuthUser("user_admin1", "admin1@gmail.com", encoder.encode("adminpass1"), r_admin);
        AuthUser u_admin2 = new AuthUser("user_admin2", "admin2@gmail.com", encoder.encode("adminpass2"), r_admin);

        List<AuthUser> adminUsers = List.of(u_admin1, u_admin2);
        authUserRepository.saveAll(adminUsers);

        // Creazione degli Admin collegati agli AuthUser
        Admin admin1 = new Admin();
        admin1.setFullName("Admin FullName 1");
        admin1.setAuthUser(u_admin1);

        Admin admin2 = new Admin();
        admin2.setFullName("Admin FullName 2");
        admin2.setAuthUser(u_admin2);

        List<Admin> admins = List.of(admin1, admin2);
        adminRepository.saveAll(admins);

        // Creazione degli AuthUser per gli studenti
        AuthUser u_customer1 = new AuthUser("user_customer1", "customer1@gmail.com", encoder.encode("password1"), r_customer);
        AuthUser u_customer2 = new AuthUser("user_customer2", "customer2@gmail.com", encoder.encode("password2"), r_customer);
        AuthUser u_customer3 = new AuthUser("user_customer3", "customer3@gmail.com", encoder.encode("password3"), r_customer);
        AuthUser u_customer4 = new AuthUser("user_customer4", "customer4@gmail.com", encoder.encode("password4"), r_customer);
        AuthUser u_customer5 = new AuthUser("user_customer5", "customer5@gmail.com", encoder.encode("password5"), r_customer);

        List<AuthUser> customerUsers = List.of(u_customer1, u_customer2, u_customer3, u_customer4, u_customer5);
        authUserRepository.saveAll(customerUsers);

        // Creazione degli studenti collegati agli AuthUser
        Customer customer1 = new Customer();
        customer1.setFirstname("Customer Firstname 1");
        customer1.setLastname("Customer Lastname 1");
        customer1.setBirthdate(LocalDate.of(2000, 1, 15));
        customer1.setAuthUser(u_customer1);

        Customer customer2 = new Customer();
        customer2.setFirstname("Customer Firstname 2");
        customer2.setLastname("Customer Lastname 2");
        customer2.setBirthdate(LocalDate.of(1999, 4, 10));
        customer2.setAuthUser(u_customer2);

        Customer customer3 = new Customer();
        customer3.setFirstname("Customer Firstname 3");
        customer3.setLastname("Customer Lastname 3");
        customer3.setBirthdate(LocalDate.of(2001, 6, 5));
        customer3.setAuthUser(u_customer3);

        Customer customer4 = new Customer();
        customer4.setFirstname("Customer Firstname 4");
        customer4.setLastname("Customer Lastname 4");
        customer4.setBirthdate(LocalDate.of(1998, 9, 20));
        customer4.setAuthUser(u_customer4);

        Customer customer5 = new Customer();
        customer5.setFirstname("Customer Firstname 5");
        customer5.setLastname("Customer Lastname 5");
        customer5.setBirthdate(LocalDate.of(2002, 12, 30));
        customer5.setAuthUser(u_customer5);

        List<Customer> customers = List.of(customer1, customer2, customer3, customer4, customer5);
        customerRepository.saveAll(customers);

        // Creazione dei DayType
        DayType holiday = new DayType();
        holiday.setName("holiday");
        holiday.setOpenTime(null);
        holiday.setCloseTime(null);
        holiday.setOpen(false);

        DayType weekend = new DayType();
        weekend.setName("weekend");
        weekend.setOpenTime(LocalTime.of(10, 0));
        weekend.setCloseTime(LocalTime.of(20, 0));
        weekend.setOpen(true);

        DayType summer = new DayType();
        summer.setName("summer");
        summer.setOpenTime(LocalTime.of(9, 0));
        summer.setCloseTime(LocalTime.of(19, 0));
        summer.setOpen(true);

        DayType winter = new DayType();
        winter.setName("winter");
        winter.setOpenTime(LocalTime.of(10, 0));
        winter.setCloseTime(LocalTime.of(18, 0));
        winter.setOpen(true);

        List<DayType> dayTypes = List.of(holiday, weekend, summer, winter);
        dayTypeRepository.saveAll(dayTypes);

        // Creazione dei Wallet
        Wallet wallet1 = new Wallet();
        wallet1.setBalance(new BigDecimal("100.00"));
        wallet1.setCreatedAt(LocalDateTime.now());
        wallet1.setUpdatedAt(null);
        wallet1.setCustomer(customer1);

        Wallet wallet2 = new Wallet();
        wallet2.setBalance(new BigDecimal("250.50"));
        wallet2.setCreatedAt(LocalDateTime.now());
        wallet2.setUpdatedAt(null);
        wallet2.setCustomer(customer2);

        Wallet wallet3 = new Wallet();
        wallet3.setBalance(new BigDecimal("55.00"));
        wallet3.setCreatedAt(LocalDateTime.now());
        wallet3.setUpdatedAt(null);
        wallet3.setCustomer(customer3);

        Wallet wallet4 = new Wallet();
        wallet4.setBalance(new BigDecimal("500.00"));
        wallet4.setCreatedAt(LocalDateTime.now());
        wallet4.setUpdatedAt(null);
        wallet4.setCustomer(customer4);

        Wallet wallet5 = new Wallet();
        wallet5.setBalance(new BigDecimal("75.25"));
        wallet5.setCreatedAt(LocalDateTime.now());
        wallet5.setUpdatedAt(null);
        wallet5.setCustomer(customer5);

        List<Wallet> wallets = List.of(wallet1, wallet2, wallet3, wallet4, wallet5);
        walletRepository.saveAll(wallets);

        // Timeout di 10 secondi
        Thread.sleep(10_000);

        // Aggiorna solo wallet3
        wallet3.setBalance(new BigDecimal("110.00"));
        wallet3.setUpdatedAt(LocalDateTime.now());
        walletRepository.save(wallet3);

        // Creazione dei Restaurant
        Restaurant restaurant1 = new Restaurant();
        restaurant1.setName("Pizzeria Bella Napoli");
        restaurant1.setKitchenType("Italian");
        restaurant1.setBookings(null);

        Restaurant restaurant2 = new Restaurant();
        restaurant2.setName("Sakura Sushi");
        restaurant2.setKitchenType("Japanese");
        restaurant2.setBookings(null);

        Restaurant restaurant3 = new Restaurant();
        restaurant3.setName("El Asador");
        restaurant3.setKitchenType("Argentinian");
        restaurant3.setBookings(null);

        List<Restaurant> restaurants = List.of(restaurant1, restaurant2, restaurant3);
        restaurantRepository.saveAll(restaurants);

        // Creazione dei TypeService
        TypeService typeService1 = new TypeService();
        typeService1.setType(AvailabilityStatus.BOOKABLE);
        typeServiceRepository.save(typeService1);

        TypeService typeService2 = new TypeService();
        typeService2.setType(AvailabilityStatus.NON_BOOKABLE);
        typeServiceRepository.save(typeService2);

        // Creazione dei NonBookable
        NonBookable nonBookable1 = new NonBookable();
        nonBookable1.setName("Wi-Fi");
        nonBookable1.setTypeService(typeService2);

        NonBookable nonBookable2 = new NonBookable();
        nonBookable2.setName("Parking Lot");
        nonBookable2.setTypeService(typeService2);

        NonBookable nonBookable3 = new NonBookable();
        nonBookable3.setName("Public Restrooms");
        nonBookable3.setTypeService(typeService2);

        NonBookable nonBookable4 = new NonBookable();
        nonBookable4.setName("Information Desk");
        nonBookable4.setTypeService(typeService2);

        NonBookable nonBookable5 = new NonBookable();
        nonBookable5.setName("Lounge Area");
        nonBookable5.setTypeService(typeService2);

        NonBookable nonBookable6 = new NonBookable();
        nonBookable6.setName("First Aid Station");
        nonBookable6.setTypeService(typeService2);

        NonBookable nonBookable7 = new NonBookable();
        nonBookable7.setName("Emergency Exit");
        nonBookable7.setTypeService(typeService2);

        NonBookable nonBookable8 = new NonBookable();
        nonBookable8.setName("Helipad");
        nonBookable8.setTypeService(typeService2);

        NonBookable nonBookable9 = new NonBookable();
        nonBookable9.setName("Lost and Found");
        nonBookable9.setTypeService(typeService2);

        NonBookable nonBookable10 = new NonBookable();
        nonBookable10.setName("Security Office");
        nonBookable10.setTypeService(typeService2);

        NonBookable nonBookable11 = new NonBookable();
        nonBookable11.setName("ATM");
        nonBookable11.setTypeService(typeService2);

        NonBookable nonBookable12 = new NonBookable();
        nonBookable12.setName("Guest Relations");
        nonBookable12.setTypeService(typeService2);

        NonBookable nonBookable13 = new NonBookable();
        nonBookable13.setName("Locker Area");
        nonBookable13.setTypeService(typeService2);

        NonBookable nonBookable14 = new NonBookable();
        nonBookable14.setName("Water Fountains");
        nonBookable14.setTypeService(typeService2);

        NonBookable nonBookable15 = new NonBookable();
        nonBookable15.setName("Charging Stations");
        nonBookable15.setTypeService(typeService2);

        List<NonBookable> nonBookableServices = List.of(nonBookable1, nonBookable2, nonBookable3, nonBookable4, nonBookable5, nonBookable6, nonBookable7, nonBookable8, nonBookable9, nonBookable10, nonBookable11, nonBookable12, nonBookable13, nonBookable14, nonBookable15
        );

        nonBookableRepository.saveAll(nonBookableServices);

        // Creazione dei Bookable
        Bookable bookable1 = new Bookable();
        bookable1.setName("Shuttle Service");
        bookable1.setPrice(new BigDecimal("5.00"));
        bookable1.setTypeService(typeService1);

        Bookable bookable2 = new Bookable();
        bookable2.setName("Picnic Area Reservation");
        bookable2.setPrice(new BigDecimal("12.00"));
        bookable2.setTypeService(typeService1);

        List<Bookable> bookableServices = List.of(bookable1, bookable2);
        bookableRepository.saveAll(bookableServices);

        // Creazione delle Zone
        Zone zone1 = new Zone();
        zone1.setName("Titanic Floodplains");
        zone1.setDescription("Vast, ancient floodplains where colossal herbivores graze and mighty predators reign the open lands.");

        Zone zone2 = new Zone();
        zone2.setName("Shadowfang Forest");
        zone2.setDescription("Dense, misty conifer forests alive with stealthy hunters and strange feathered creatures.");

        Zone zone3 = new Zone();
        zone3.setName("Primeval Marshes");
        zone3.setDescription("Swampy wetlands rich in plant life and ancient semi-aquatic giants lurking beneath the murky waters.");

        Zone zone4 = new Zone();
        zone4.setName("Crystal Coast Lagoons");
        zone4.setDescription("Sparkling coastal lagoons teeming with marine reptiles, ancient turtles, and prehistoric sea birds.");

        Zone zone5 = new Zone();
        zone5.setName("Abyssal Ocean Realm");
        zone5.setDescription("Dark, deep ocean depths dominated by monstrous sea predators ruling the underwater kingdom.");

        Zone zone6 = new Zone();
        zone6.setName("Wings Over the Cretaceous");
        zone6.setDescription("The ancient skies filled with flying reptiles and early birds, rulers of the air and shoreline cliffs.");

        Zone zone7 = new Zone();
        zone7.setName("Visitor Hub");
        zone7.setDescription("The heart of the park, where exploration begins — a place to learn, relax, and prepare for adventure.");

        List<Zone> zones = List.of(zone1, zone2, zone3, zone4, zone5, zone6, zone7);
        zoneRepository.saveAll(zones);

        // Creazione delle Attraction

        // Titanic Floodplains - herbivores
        Attraction argentinosaurus = new Attraction();
        argentinosaurus.setName("Argentinosaurus");
        argentinosaurus.setDescription("Argentinosaurus huinculensis – A colossal sauropod herbivore from the Late Cretaceous (94 million years ago). Estimated up to 35 meters in length. Diet: ferns, conifers, cycads. One of the largest land animals ever, roaming the floodplains of South America. Habitat: warm, semi-arid floodplains with seasonal rivers and vegetation.");
        argentinosaurus.setDangerLevel(DangerLevel.LOW);
        argentinosaurus.setZone(zone1);
        attractionRepository.save(argentinosaurus);

        Attraction triceratops = new Attraction();
        triceratops.setName("Triceratops");
        triceratops.setDescription("Triceratops horridus – Large herbivore with three horns and a bony frill for defense. Late Cretaceous (~68-66 Mya). Length: ~9 meters. Diet: low-lying plants and shrubs. Habitat: subtropical floodplains and open forests.");
        triceratops.setDangerLevel(DangerLevel.LOW);
        triceratops.setZone(zone1);
        attractionRepository.save(triceratops);

        // Titanic Floodplains - carnivores
        Attraction tyrannosaurus = new Attraction();
        tyrannosaurus.setName("Tyrannosaurus rex");
        tyrannosaurus.setDescription("Tyrannosaurus rex – Apex predator of the Late Cretaceous (68–66 Mya). Length: ~12 meters. Diet: large herbivores. Known for its massive skull and bone-crushing bite. Roamed North America’s lush plains. Habitat: subtropical forests and coastal lowlands.");
        tyrannosaurus.setDangerLevel(DangerLevel.EXTREME);
        tyrannosaurus.setZone(zone1);
        attractionRepository.save(tyrannosaurus);

        Attraction carnotaurus = new Attraction();
        carnotaurus.setName("Carnotaurus");
        carnotaurus.setDescription("Carnotaurus sastrei – Fast predator known for its distinct horns above the eyes and lightweight build. Late Cretaceous of South America (~72-69 Mya). Habitat: dry forests and floodplain areas.");
        carnotaurus.setDangerLevel(DangerLevel.HIGH);
        carnotaurus.setZone(zone1);
        attractionRepository.save(carnotaurus);

        // Shadowfang Forest - carnivores
        Attraction velociraptor = new Attraction();
        velociraptor.setName("Velociraptor");
        velociraptor.setDescription("Velociraptor mongoliensis – Small, feathered, highly intelligent hunter from the Late Cretaceous (~75-71 Mya). Length: ~2 meters. Diet: small animals and scavenging. Habitat: arid to semi-arid desert regions with sparse vegetation.");
        velociraptor.setDangerLevel(DangerLevel.HIGH);
        velociraptor.setZone(zone2);
        attractionRepository.save(velociraptor);

        Attraction oviraptor = new Attraction();
        oviraptor.setName("Oviraptor");
        oviraptor.setDescription("Oviraptor philoceratops – Feathered dinosaur known for cleverness and agility. Late Cretaceous (~75-71 Mya). Diet: omnivorous, possibly eggs and small animals. Habitat: arid desert environments with sand dunes and river valleys.");
        oviraptor.setDangerLevel(DangerLevel.MEDIUM);
        oviraptor.setZone(zone2);
        attractionRepository.save(oviraptor);

        Attraction therizinosaurus = new Attraction();
        therizinosaurus.setName("Therizinosaurus");
        therizinosaurus.setDescription("Therizinosaurus cheloniformis – Unusual theropod with huge claws, herbivorous diet, and feathered body. Late Cretaceous (~70 Mya). Habitat: lush forests and floodplains.");
        therizinosaurus.setDangerLevel(DangerLevel.HIGH);
        therizinosaurus.setZone(zone2);
        attractionRepository.save(therizinosaurus);

        // Primeval Marshes
        Attraction spinosaurus = new Attraction();
        spinosaurus.setName("Spinosaurus");
        spinosaurus.setDescription("Spinosaurus aegyptiacus – Semi-aquatic predator of the mid-Cretaceous (~112-93 Mya). Length: up to 15 meters. Diet: fish and small dinosaurs. Habitat: river deltas and swampy marshlands.");
        spinosaurus.setDangerLevel(DangerLevel.EXTREME);
        spinosaurus.setZone(zone3);
        attractionRepository.save(spinosaurus);

        Attraction ouranosaurus = new Attraction();
        ouranosaurus.setName("Ouranosaurus");
        ouranosaurus.setDescription("Ouranosaurus nigeriensis – Herbivore with distinctive sail-back from the mid-Cretaceous (~112-93 Mya). Length: ~7 meters. Diet: soft plants. Habitat: wetlands and marshy floodplains.");
        ouranosaurus.setDangerLevel(DangerLevel.LOW);
        ouranosaurus.setZone(zone3);
        attractionRepository.save(ouranosaurus);

        // Crystal Coast Lagoons
        Attraction mosasaurus = new Attraction();
        mosasaurus.setName("Mosasaurus");
        mosasaurus.setDescription("Mosasaurus hoffmannii – Giant marine predator ruling lagoon waters in the Late Cretaceous (~70-66 Mya). Length: up to 17 meters. Habitat: warm shallow seas and lagoons.");
        mosasaurus.setDangerLevel(DangerLevel.EXTREME);
        mosasaurus.setZone(zone4);
        attractionRepository.save(mosasaurus);

        Attraction elasmosaurus = new Attraction();
        elasmosaurus.setName("Elasmosaurus");
        elasmosaurus.setDescription("Elasmosaurus platyurus – Long-necked marine reptile gliding in lagoon waters. Late Cretaceous (~80-65 Mya). Length: ~14 meters. Diet: fish and small marine animals. Habitat: coastal lagoons and shallow seas.");
        elasmosaurus.setDangerLevel(DangerLevel.HIGH);
        elasmosaurus.setZone(zone4);
        attractionRepository.save(elasmosaurus);

        Attraction archelon = new Attraction();
        archelon.setName("Archelon");
        archelon.setDescription("Archelon ischyros – Massive prehistoric sea turtle from the Late Cretaceous (~75-65 Mya). Length: up to 4 meters. Habitat: warm coastal waters and lagoons.");
        archelon.setDangerLevel(DangerLevel.LOW);
        archelon.setZone(zone4);
        attractionRepository.save(archelon);

        Attraction hesperornis = new Attraction();
        hesperornis.setName("Hesperornis");
        hesperornis.setDescription("Hesperornis regalis – Flightless aquatic bird adapted to lagoon life. Late Cretaceous (~85-70 Mya). Habitat: coastal lagoons and shallow seas.");
        hesperornis.setDangerLevel(DangerLevel.LOW);
        hesperornis.setZone(zone4);
        attractionRepository.save(hesperornis);

        Attraction ichthyornis = new Attraction();
        ichthyornis.setName("Ichthyornis");
        ichthyornis.setDescription("Ichthyornis dispar – Early flying bird living near lagoon shores. Late Cretaceous (~95-85 Mya). Habitat: coastal wetlands and lagoons.");
        ichthyornis.setDangerLevel(DangerLevel.LOW);
        ichthyornis.setZone(zone4);
        attractionRepository.save(ichthyornis);

        Attraction ammonites = new Attraction();
        ammonites.setName("Giant Ammonites");
        ammonites.setDescription("Giant Ammonites – Huge shelled cephalopods spiraling in lagoon depths. Late Cretaceous marine inhabitants.");
        ammonites.setDangerLevel(DangerLevel.LOW);
        ammonites.setZone(zone4);
        attractionRepository.save(ammonites);

        // Abyssal Ocean Realm
        Attraction kronosaurus = new Attraction();
        kronosaurus.setName("Kronosaurus");
        kronosaurus.setDescription("Kronosaurus queenslandicus – Massive pliosaur patrolling the deep ocean. Early Cretaceous (~120-100 Mya). Length: up to 10 meters. Habitat: deep marine waters.");
        kronosaurus.setDangerLevel(DangerLevel.EXTREME);
        kronosaurus.setZone(zone5);
        attractionRepository.save(kronosaurus);

        Attraction xiphactinus = new Attraction();
        xiphactinus.setName("Xiphactinus");
        xiphactinus.setDescription("Xiphactinus audax – Large predatory fish with sharp teeth. Late Cretaceous (~112-66 Mya). Habitat: open ocean waters.");
        xiphactinus.setDangerLevel(DangerLevel.HIGH);
        xiphactinus.setZone(zone5);
        attractionRepository.save(xiphactinus);

        // Wings Over the Cretaceous
        Attraction pterosaurs = new Attraction();
        pterosaurs.setName("Pterosaurs");
        pterosaurs.setDescription("Various Pterosaur species – Flying reptiles soaring over cliffs and seas. Late Cretaceous. Habitat: coastal cliffs, open skies, and marine environments.");
        pterosaurs.setDangerLevel(DangerLevel.MEDIUM);
        pterosaurs.setZone(zone6);
        attractionRepository.save(pterosaurs);

        Attraction hesperornisSky = new Attraction();
        hesperornisSky.setName("Hesperornis (Flying)");
        hesperornisSky.setDescription("Hesperornis regalis – Early aquatic bird capable of flight over shorelines. Late Cretaceous (~85-70 Mya). Habitat: coastal waters and lagoons.");
        hesperornisSky.setDangerLevel(DangerLevel.LOW);
        hesperornisSky.setZone(zone6);
        attractionRepository.save(hesperornisSky);

        Attraction ichthyornisSky = new Attraction();
        ichthyornisSky.setName("Ichthyornis (Flying)");
        ichthyornisSky.setDescription("Ichthyornis dispar – Ancient bird gliding above lagoon and coastal areas. Late Cretaceous (~95-85 Mya). Habitat: coastal wetlands.");
        ichthyornisSky.setDangerLevel(DangerLevel.LOW);
        ichthyornisSky.setZone(zone6);
        attractionRepository.save(ichthyornisSky);

        // Visitor Hub - safe zone
        Attraction museum = new Attraction();
        museum.setName("Museum");
        museum.setDescription("A place to discover the park’s history and the secrets of the Cretaceous era.");
        museum.setDangerLevel(DangerLevel.MAYBE_SAFE_MAYBE_THERE_ARE_RAPTORS);
        museum.setZone(zone7);
        attractionRepository.save(museum);

        Attraction geneticsLab = new Attraction();
        geneticsLab.setName("Genetics Lab");
        geneticsLab.setDescription("Get a glimpse into the science behind the dinosaurs. Witness DNA sequencing, embryo incubation, and ask our scientists questions — as long as they’re not hiding something.");
        geneticsLab.setDangerLevel(DangerLevel.MAYBE_SAFE_MAYBE_THERE_ARE_RAPTORS);
        geneticsLab.setZone(zone7);
        attractionRepository.save(geneticsLab);


        Attraction restaurant = new Attraction();
        restaurant.setName("Restaurants");
        restaurant.setDescription("Where you can enjoy dishes from all around the world.");
        restaurant.setDangerLevel(DangerLevel.MAYBE_SAFE_MAYBE_THERE_ARE_RAPTORS);
        restaurant.setZone(zone7);
        attractionRepository.save(restaurant);

        Attraction shop = new Attraction();
        shop.setName("Shop");
        shop.setDescription("Souvenir store with gadgets and keepsakes from the park.");
        shop.setDangerLevel(DangerLevel.MAYBE_SAFE_MAYBE_THERE_ARE_RAPTORS);
        shop.setZone(zone7);
        attractionRepository.save(shop);

        // Creazione degli Show
        Show thunderOfTitans = new Show();
        thunderOfTitans.setName("Thunder of Titans");
        thunderOfTitans.setDescription("Watch the colossal Argentinosaurus stomp through the floodplains in a choreographed display of might and grace.");
        thunderOfTitans.setAttraction("Argentinosaurus");
        thunderOfTitans.setTime(LocalTime.of(10, 0));
        thunderOfTitans.setZone(zone1);
        showRepository.save(thunderOfTitans);

        Show hornedDefenseDemo = new Show();
        hornedDefenseDemo.setName("Horned Defense Demonstration");
        hornedDefenseDemo.setDescription("Learn how Triceratops uses its horns and frill in mock battles for dominance and protection.");
        hornedDefenseDemo.setAttraction("Triceratops");
        hornedDefenseDemo.setTime(LocalTime.of(12, 30));
        hornedDefenseDemo.setZone(zone1);
        showRepository.save(hornedDefenseDemo);

        Show predatorKings = new Show();
        predatorKings.setName("Predator Kings");
        predatorKings.setDescription("See the ferocious Tyrannosaurus rex in action during a simulated hunt and feeding.");
        predatorKings.setAttraction("Tyrannosaurus rex");
        predatorKings.setTime(LocalTime.of(15, 0));
        predatorKings.setZone(zone1);
        showRepository.save(predatorKings);

        Show raptorTactics = new Show();
        raptorTactics.setName("Raptor Tactics");
        raptorTactics.setDescription("Experience the coordination and cunning of Velociraptors in a live demonstration of pack hunting.");
        raptorTactics.setAttraction("Velociraptor");
        raptorTactics.setTime(LocalTime.of(11, 0));
        raptorTactics.setZone(zone2);
        showRepository.save(raptorTactics);

        Show clawsAndFeathers = new Show();
        clawsAndFeathers.setName("Claws and Feathers");
        clawsAndFeathers.setDescription("Discover the unique biology of Therizinosaurus with a live showcase of its bizarre claws and feathered movement.");
        clawsAndFeathers.setAttraction("Therizinosaurus");
        clawsAndFeathers.setTime(LocalTime.of(14, 0));
        clawsAndFeathers.setZone(zone2);
        showRepository.save(clawsAndFeathers);

        Show swampAmbush = new Show();
        swampAmbush.setName("Swamp Ambush");
        swampAmbush.setDescription("Spinosaurus reveals its stealth and power in a mock ambush within the murky marshes.");
        swampAmbush.setAttraction("Spinosaurus");
        swampAmbush.setTime(LocalTime.of(13, 0));
        swampAmbush.setZone(zone3);
        showRepository.save(swampAmbush);

        Show sailbackParade = new Show();
        sailbackParade.setName("Sailback Parade");
        sailbackParade.setDescription("Marvel at the Ouranosaurus as they move in herds, flaunting their distinctive sail-like backs.");
        sailbackParade.setAttraction("Ouranosaurus");
        sailbackParade.setTime(LocalTime.of(10, 30));
        sailbackParade.setZone(zone3);
        showRepository.save(sailbackParade);

        Show marineMonsters = new Show();
        marineMonsters.setName("Marine Monsters of the Lagoon");
        marineMonsters.setDescription("Watch the terrifying Mosasaurus breach the water in a thrilling feeding display.");
        marineMonsters.setAttraction("Mosasaurus");
        marineMonsters.setTime(LocalTime.of(16, 0));
        marineMonsters.setZone(zone4);
        showRepository.save(marineMonsters);

        Show turtleTime = new Show();
        turtleTime.setName("Turtle Time");
        turtleTime.setDescription("A peaceful dive into the habits and history of Archelon, the gentle sea giant.");
        turtleTime.setAttraction("Archelon");
        turtleTime.setTime(LocalTime.of(11, 45));
        turtleTime.setZone(zone4);
        showRepository.save(turtleTime);

        Show deepSeaTerror = new Show();
        deepSeaTerror.setName("Deep Sea Terror");
        deepSeaTerror.setDescription("Brace yourself as the Kronosaurus emerges from the depths in a thrilling simulated attack.");
        deepSeaTerror.setAttraction("Kronosaurus");
        deepSeaTerror.setTime(LocalTime.of(15, 30));
        deepSeaTerror.setZone(zone5);
        showRepository.save(deepSeaTerror);

        Show skyRulers = new Show();
        skyRulers.setName("Sky Rulers");
        skyRulers.setDescription("Pterosaurs soar above the cliffs in a dazzling aerial show with dives, glides, and shrieks.");
        skyRulers.setAttraction("Pterosaurs");
        skyRulers.setTime(LocalTime.of(17, 0));
        skyRulers.setZone(zone6);
        showRepository.save(skyRulers);

        Show flyingRelatives = new Show();
        flyingRelatives.setName("Flying Relatives");
        flyingRelatives.setDescription("Meet Ichthyornis in an interactive presentation on how ancient birds relate to modern ones.");
        flyingRelatives.setAttraction("Ichthyornis (Flying)");
        flyingRelatives.setTime(LocalTime.of(9, 30));
        flyingRelatives.setZone(zone6);
        showRepository.save(flyingRelatives);

        // Show Visitor Hub
        Show fossilsUncovered = new Show();
        fossilsUncovered.setName("Fossils Uncovered");
        fossilsUncovered.setDescription("Join our expert paleontologist for a deep dive into real Cretaceous fossils. Absolutely no chance of a live Velociraptor interrupting. Probably.");
        fossilsUncovered.setAttraction("Cretaceous Museum");
        fossilsUncovered.setTime(LocalTime.of(10, 15));
        fossilsUncovered.setZone(zone7);
        showRepository.save(fossilsUncovered);

        Show dnaToDino = new Show();
        dnaToDino.setName("From DNA to Dino");
        dnaToDino.setDescription("Discover how prehistoric DNA brought the park to life. Interactive holograms, no actual cloning — unless Dr. Wu says otherwise.");
        dnaToDino.setAttraction("Cretaceous Museum");
        dnaToDino.setTime(LocalTime.of(13, 45));
        dnaToDino.setZone(zone7);
        showRepository.save(dnaToDino);

        Show extinctionLive = new Show();
        extinctionLive.setName("Extinction Live!");
        extinctionLive.setDescription("Experience the end of the dinosaurs with thrilling effects. Warning: Includes loud noises, meteor impacts, and zero Jeff Goldblum.");
        extinctionLive.setAttraction("Cretaceous Museum");
        extinctionLive.setTime(LocalTime.of(15, 30));
        extinctionLive.setZone(zone7);
        showRepository.save(extinctionLive);

        Show dinoTrivia = new Show();
        dinoTrivia.setName("Dino Trivia Show");
        dinoTrivia.setDescription("Think you know more than Alan Grant? Test your knowledge and win exclusive prizes. No fences failed... yet.");
        dinoTrivia.setAttraction("Cretaceous Museum");
        dinoTrivia.setTime(LocalTime.of(17, 0));
        dinoTrivia.setZone(zone7);
        showRepository.save(dinoTrivia);

        Show meetTheGeneticists = new Show();
        meetTheGeneticists.setName("Meet the Geneticists");
        meetTheGeneticists.setDescription("Our scientists will walk you through the incredible process of cloning dinosaurs — and maybe accidentally reveal a corporate secret or two. Come see dinosaur's eggs!");
        meetTheGeneticists.setAttraction("Genetics Lab");
        meetTheGeneticists.setTime(LocalTime.of(11, 0));
        meetTheGeneticists.setZone(zone7);
        showRepository.save(meetTheGeneticists);

        Show buildADino = new Show();
        buildADino.setName("Build-a-Dino™ Workshop");
        buildADino.setDescription("Customize your very own dinosaur genome! Don't worry, it won't hatch... probably. WARNING: strictly visual and theoretical");
        buildADino.setAttraction("Genetics Lab");
        buildADino.setTime(LocalTime.of(15, 0));
        buildADino.setZone(zone7);
        showRepository.save(buildADino);

        //Creazione dei Planner

        Planner planner1 = new Planner();
        planner1.setCustomer(customer1);
        planner1.setDate(LocalDate.of(2025, 7,7));
        planner1.setDayType(summer);

        Planner planner2 = new Planner();
        planner2.setCustomer(customer2);
        planner2.setDate(LocalDate.of(2025, 7,7));
        planner2.setDayType(summer);

        Planner planner3 = new Planner();
        planner3.setCustomer(customer3);
        planner3.setDate(LocalDate.of(2025, 7,7));
        planner3.setDayType(summer);

        List<Planner> planners = List.of(planner1, planner2, planner3);
        plannerRepository.saveAll(planners);

        // Creazione dei Ticket
        Ticket ticket1 = new Ticket();
        ticket1.setDate(LocalDate.of(2025, 7,7));
        ticket1.setPrice(new BigDecimal("20.00"));
        ticket1.setAttraction(tyrannosaurus);
        ticket1.setPlanner(planner2);

        Ticket ticket2 = new Ticket();
        ticket2.setDate(LocalDate.of(2025, 7,7));
        ticket2.setPrice(new BigDecimal("20.00"));
        ticket2.setAttraction(tyrannosaurus);
        ticket2.setPlanner(planner1);

        Ticket ticket3 = new Ticket();
        ticket3.setDate(LocalDate.of(2025, 7,7));
        ticket3.setPrice(new BigDecimal("20.00"));
        ticket3.setAttraction(tyrannosaurus);
        ticket2.setPlanner(planner1);

        Ticket ticket4 = new Ticket();
        ticket4.setDate(LocalDate.of(2025, 7,7));
        ticket4.setPrice(new BigDecimal("20.00"));
        ticket4.setAttraction(mosasaurus);
        ticket4.setPlanner(planner3);

        Ticket ticket5 = new Ticket();
        ticket5.setDate(LocalDate.of(2025, 7,7));
        ticket5.setPrice(new BigDecimal("20.00"));
        ticket5.setAttraction(mosasaurus);
        ticket5.setPlanner(planner3);

        Ticket ticket6 = new Ticket();
        ticket6.setDate(LocalDate.of(2025, 7,7));
        ticket6.setPrice(new BigDecimal("20.00"));
        ticket6.setAttraction(mosasaurus);
        ticket6.setPlanner(planner3);

        Ticket ticket7 = new Ticket();
        ticket7.setDate(LocalDate.of(2025, 7,7));
        ticket7.setPrice(new BigDecimal("20.00"));
        ticket7.setAttraction(velociraptor);

        Ticket ticket8 = new Ticket();
        ticket8.setDate(LocalDate.of(2025, 7,7));
        ticket8.setPrice(new BigDecimal("20.00"));
        ticket8.setAttraction(velociraptor);

        Ticket ticket9 = new Ticket();
        ticket9.setDate(LocalDate.of(2025, 7,7));
        ticket9.setPrice(new BigDecimal("20.00"));
        ticket9.setAttraction(velociraptor);
        ticket9.setPlanner(planner2);

        Ticket ticket10 = new Ticket();
        ticket10.setDate(LocalDate.of(2025, 7,7));
        ticket10.setPrice(new BigDecimal("20.00"));
        ticket10.setAttraction(triceratops);

        Ticket ticket11 = new Ticket();
        ticket11.setDate(LocalDate.of(2025, 7,7));
        ticket11.setPrice(new BigDecimal("20.00"));
        ticket11.setAttraction(triceratops);
        ticket2.setPlanner(planner1);

        Ticket ticket12 = new Ticket();
        ticket12.setDate(LocalDate.of(2025, 7,7));
        ticket12.setPrice(new BigDecimal("20.00"));
        ticket12.setAttraction(triceratops);
        ticket2.setPlanner(planner1);

        Ticket ticket13 = new Ticket();
        ticket13.setDate(LocalDate.of(2025, 7,7));
        ticket13.setPrice(new BigDecimal("20.00"));
        ticket13.setShow(marineMonsters);

        Ticket ticket14 = new Ticket();
        ticket14.setDate(LocalDate.of(2025, 7,7));
        ticket14.setPrice(new BigDecimal("20.00"));
        ticket14.setShow(marineMonsters);

        Ticket ticket15 = new Ticket();
        ticket15.setDate(LocalDate.of(2025, 7,7));
        ticket15.setPrice(new BigDecimal("20.00"));
        ticket15.setShow(marineMonsters);
        ticket15.setPlanner(planner1);

        Ticket ticket16 = new Ticket();
        ticket16.setDate(LocalDate.of(2025, 7,7));
        ticket16.setPrice(new BigDecimal("20.00"));
        ticket16.setShow(marineMonsters);
        ticket16.setPlanner(planner1);

        Ticket ticket17 = new Ticket();
        ticket17.setDate(LocalDate.of(2025, 7,7));
        ticket17.setPrice(new BigDecimal("20.00"));
        ticket17.setShow(marineMonsters);

        Ticket ticket18 = new Ticket();
        ticket18.setDate(LocalDate.of(2025, 7,7));
        ticket18.setPrice(new BigDecimal("20.00"));
        ticket18.setShow(marineMonsters);
        ticket18.setPlanner(planner2);

        Ticket ticket19 = new Ticket();
        ticket19.setDate(LocalDate.of(2025, 7,7));
        ticket19.setPrice(new BigDecimal("20.00"));
        ticket19.setShow(raptorTactics);
        ticket19.setPlanner(planner2);

        Ticket ticket20 = new Ticket();
        ticket20.setDate(LocalDate.of(2025, 7,7));
        ticket20.setPrice(new BigDecimal("20.00"));
        ticket20.setShow(raptorTactics);

        Ticket ticket21 = new Ticket();
        ticket21.setDate(LocalDate.of(2025, 7,7));
        ticket21.setPrice(new BigDecimal("20.00"));
        ticket21.setShow(raptorTactics);

        Ticket ticket22 = new Ticket();
        ticket22.setDate(LocalDate.of(2025, 7,7));
        ticket22.setPrice(new BigDecimal("20.00"));
        ticket22.setShow(raptorTactics);

        Ticket ticket23 = new Ticket();
        ticket23.setDate(LocalDate.of(2025, 7,7));
        ticket23.setPrice(new BigDecimal("20.00"));
        ticket23.setShow(raptorTactics);
        ticket23.setPlanner(planner1);

        Ticket ticket24 = new Ticket();
        ticket24.setDate(LocalDate.of(2025, 7,7));
        ticket24.setPrice(new BigDecimal("20.00"));
        ticket24.setShow(raptorTactics);
        ticket24.setPlanner(planner1);

        Ticket ticket25 = new Ticket();
        ticket25.setDate(LocalDate.of(2025, 7,7));
        ticket25.setPrice(new BigDecimal("20.00"));
        ticket25.setShow(fossilsUncovered);

        Ticket ticket26 = new Ticket();
        ticket26.setDate(LocalDate.of(2025, 7,7));
        ticket26.setPrice(new BigDecimal("20.00"));
        ticket26.setShow(fossilsUncovered);
        ticket26.setPlanner(planner1);

        Ticket ticket27 = new Ticket();
        ticket27.setDate(LocalDate.of(2025, 7,7));
        ticket27.setPrice(new BigDecimal("20.00"));
        ticket27.setShow(fossilsUncovered);
        ticket27.setPlanner(planner1);

        Ticket ticket28 = new Ticket();
        ticket28.setDate(LocalDate.of(2025, 7,7));
        ticket28.setPrice(new BigDecimal("20.00"));
        ticket28.setShow(fossilsUncovered);
        ticket28.setPlanner(planner3);

        Ticket ticket29 = new Ticket();
        ticket29.setDate(LocalDate.of(2025, 7,7));
        ticket29.setPrice(new BigDecimal("20.00"));
        ticket29.setShow(fossilsUncovered);
        ticket29.setPlanner(planner3);

        Ticket ticket30 = new Ticket();
        ticket30.setDate(LocalDate.of(2025, 7,7));
        ticket30.setPrice(new BigDecimal("20.00"));
        ticket30.setShow(fossilsUncovered);
        ticket30.setPlanner(planner3);

        List<Ticket> tickets = List.of(ticket1, ticket2, ticket3, ticket4, ticket5, ticket6, ticket7, ticket8, ticket9, ticket10, ticket11,ticket12, ticket13, ticket14, ticket15, ticket16, ticket17, ticket18, ticket19, ticket20, ticket21, ticket22, ticket23, ticket24, ticket25, ticket26, ticket27, ticket28, ticket29, ticket30);
        ticketRepository.saveAll(tickets);

        // Creazione dei Booking
        Booking booking1 = new Booking();
        booking1.setDate(LocalDate.of(2025, 7,7));
        booking1.setBookable(bookable1);
        booking1.setPlanner(planner2);

        Booking booking2 = new Booking();
        booking2.setDate(LocalDate.of(2025, 7,7));
        booking2.setBookable(bookable2);
        booking2.setPlanner(planner2);

        Booking booking3 = new Booking();
        booking3.setDate(LocalDate.of(2025, 7,7));
        booking3.setRestaurant(restaurant1);
        booking3.setPlanner(planner2);

        Booking booking4 = new Booking();
        booking4.setDate(LocalDate.of(2025, 7,7));
        booking4.setRestaurant(restaurant2);
        booking4.setReservationQty(2);
        booking4.setPlanner(planner1);

        Booking booking5 = new Booking();
        booking5.setDate(LocalDate.of(2025, 7,7));
        booking5.setRestaurant(restaurant3);
        booking5.setReservationQty(3);
        booking5.setPlanner(planner3);

        List<Booking> bookings = List.of(booking1, booking2, booking3, booking4, booking5);
        bookingRepository.saveAll(bookings);

    }

}

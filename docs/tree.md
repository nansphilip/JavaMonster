.
├── docs
│ ├── instruction .md
│ ├── instruction.md
│ ├── models.md
│ ├── pseudocode.md
│ ├── reglesV1.02.md
│ └── rules.md
├── lombok.jar
├── package-lock.json
├── pom.xml
├── README.md
├── src
│ ├── main
│ │ ├── java
│ │ │ └── com
│ │ │     └── fantasyhospital
│ │ │         ├── config
│ │ │         │ ├── ApplicationConfig.java
│ │ │         │ ├── FxmlLoader.java
│ │ │         │ ├── FxmlView.java
│ │ │         │ └── StageManager.java
│ │ │         ├── controller
│ │ │         │ ├── ConsoleLogController.java
│ │ │         │ ├── CounterController.java
│ │ │         │ ├── CryptDetailsController.java
│ │ │         │ ├── CryptViewController.java
│ │ │         │ ├── DoomController.java
│ │ │         │ ├── EndGameLogController.java
│ │ │         │ ├── GridMedicalServiceController.java
│ │ │         │ ├── HospitalStructureController.java
│ │ │         │ ├── ListCreatureController.java
│ │ │         │ ├── ListDoctorsController.java
│ │ │         │ ├── LogoViewController.java
│ │ │         │ ├── MedicalServiceDetailsController.java
│ │ │         │ ├── QuarantineDetailsController.java
│ │ │         │ ├── QuarantineViewController.java
│ │ │         │ ├── ui
│ │ │         │ │ └── ToolbarController.java
│ │ │         │ └── WaitingRoomController.java
│ │ │         ├── enums
│ │ │         │ ├── ActionType.java
│ │ │         │ ├── BudgetType.java
│ │ │         │ ├── DiseaseType.java
│ │ │         │ ├── FemaleNameType.java
│ │ │         │ ├── GenderType.java
│ │ │         │ ├── MaleNameType.java
│ │ │         │ ├── RaceType.java
│ │ │         │ ├── ServiceNameType.java
│ │ │         │ └── StackType.java
│ │ │         ├── EvolutionGame.java
│ │ │         ├── FantasyHospitalApplication.java
│ │ │         ├── FantasyHospitalStarter.java
│ │ │         ├── Game.java
│ │ │         ├── model
│ │ │         │ ├── creatures
│ │ │         │ │ ├── abstractclass
│ │ │         │ │ │ ├── Beast.java
│ │ │         │ │ │ ├── BeastUtils.java
│ │ │         │ │ │ └── Creature.java
│ │ │         │ │ ├── Doctor.java
│ │ │         │ │ ├── interfaces
│ │ │         │ │ │ ├── Contaminant.java
│ │ │         │ │ │ ├── Demoralizing.java
│ │ │         │ │ │ └── Regenerating.java
│ │ │         │ │ ├── races
│ │ │         │ │ │ ├── Dwarf.java
│ │ │         │ │ │ ├── Elf.java
│ │ │         │ │ │ ├── Lycanthrope.java
│ │ │         │ │ │ ├── Orc.java
│ │ │         │ │ │ ├── Reptilian.java
│ │ │         │ │ │ ├── Vampire.java
│ │ │         │ │ │ ├── Werebeast.java
│ │ │         │ │ │ └── Zombie.java
│ │ │         │ │ ├── TriageResident.java
│ │ │         │ │ └── VIPPatient.java
│ │ │         │ ├── disease
│ │ │         │ │ ├── Disease.java
│ │ │         │ │ └── DiseaseUtils.java
│ │ │         │ ├── Hospital.java
│ │ │         │ └── rooms
│ │ │         │     ├── medicalservice
│ │ │         │     │ ├── Crypt.java
│ │ │         │     │ ├── MedicalService.java
│ │ │         │     │ ├── MedicalServiceUtils.java
│ │ │         │     │ └── Quarantine.java
│ │ │         │     └── Room.java
│ │ │         ├── observer
│ │ │         │ ├── CreatureObserver.java
│ │ │         │ ├── ExitObserver.java
│ │ │         │ └── MoralObserver.java
│ │ │         ├── Simulation.java
│ │ │         ├── SimulationConsole.java
│ │ │         ├── util
│ │ │         │ ├── CropImageUtils.java
│ │ │         │ ├── EndGameSummary.java
│ │ │         │ ├── LogsUtils.java
│ │ │         │ ├── PixelTypeWriter.java
│ │ │         │ ├── RemovePngBackgroundUtils.java
│ │ │         │ └── Singleton.java
│ │ │         └── view
│ │ │             ├── CloseDoorCellView.java
│ │ │             ├── CounterCellView.java
│ │ │             ├── CreatureCellView.java
│ │ │             ├── CryptCellView.java
│ │ │             ├── DetailsCellView.java
│ │ │             ├── DoctorsCellView.java
│ │ │             ├── EndGameCellView.java
│ │ │             ├── EventCellView.java
│ │ │             ├── GifCellView.java
│ │ │             ├── MedicalServiceCellView.java
│ │ │             └── QuarantineCellView.java
│ │ └── resources
│ │     ├── fonts
│ │     │ └── FantasyHospital.ttf
│ │     ├── fxml
│ │     │ ├── consoleLogView.fxml
│ │     │ ├── counterView.fxml
│ │     │ ├── cryptDetailsListView.fxml
│ │     │ ├── cryptView.fxml
│ │     │ ├── doomView.fxml
│ │     │ ├── endGameLogView.fxml
│ │     │ ├── hospitalStructureView.fxml
│ │     │ ├── listCreatureView.fxml
│ │     │ ├── listDoctorsView.fxml
│ │     │ ├── logoView.fxml
│ │     │ ├── mainView.fxml
│ │     │ ├── medicalServiceDetailsListView.fxml
│ │     │ ├── medicalServiceView.fxml
│ │     │ ├── quarantineDetailsListView.fxml
│ │     │ ├── quarantineView.fxml
│ │     │ ├── toolbar.fxml
│ │     │ └── waitingRoomView.fxml
│ │     ├── images
│ │     │ ├── counter
│ │     │ │ ├── BudgetChest.png
│ │     │ │ ├── DeathCreature.PNG
│ │     │ │ ├── DeathDoctor.PNG
│ │     │ │ ├── Healed.png
│ │     │ │ └── Hourglass.png
│ │     │ ├── crypt
│ │     │ │ ├── CryptTemp1.png
│ │     │ │ ├── CryptTemp2.png
│ │     │ │ ├── CryptTemp3.png
│ │     │ │ ├── CryptTemp4.png
│ │     │ │ ├── CryptTemp5.png
│ │     │ │ ├── CryptTemp6.png
│ │     │ │ ├── CryptTemp7.png
│ │     │ │ ├── CryptTemp8.png
│ │     │ │ └── CryptTemp9.png
│ │     │ ├── dialogue
│ │     │ │ └── DialogueNevot.jpg
│ │     │ ├── diseases
│ │     │ │ ├── FullDiseasesBar.png
│ │     │ │ ├── Heal.png
│ │     │ │ ├── HighDiseasesBar1.png
│ │     │ │ ├── HighDiseasesBar2.png
│ │     │ │ ├── LowDiseasesBar1.png
│ │     │ │ ├── LowDiseasesBar2.png
│ │     │ │ ├── LowDiseasesBar3.png
│ │     │ │ ├── MediumDiseasesBar1.png
│ │     │ │ └── MediumDiseasesBar2.png
│ │     │ ├── gender
│ │     │ │ ├── Female.png
│ │     │ │ ├── LgbtFlag.png
│ │     │ │ └── Male.png
│ │     │ ├── gif
│ │     │ │ ├── Creature1.gif
│ │     │ │ ├── Creature2.gif
│ │     │ │ ├── Creature3.gif
│ │     │ │ ├── Creature4.gif
│ │     │ │ ├── Creature5.gif
│ │     │ │ ├── Creature6.gif
│ │     │ │ ├── Creature8.gif
│ │     │ │ ├── Harakiri.gif
│ │     │ │ ├── Heal.gif
│ │     │ │ └── MedicalServiceBomb.gif
│ │     │ ├── icon
│ │     │ │ └── Icon.png
│ │     │ ├── logo
│ │     │ │ └── Logo.jpg
│ │     │ ├── morale
│ │     │ │ ├── EmptyLifeBar.png
│ │     │ │ ├── FullLifeBar.png
│ │     │ │ ├── HighLifeBar1.png
│ │     │ │ ├── HighLifeBar2.png
│ │     │ │ ├── LowLifeBar1.png
│ │     │ │ ├── LowLifeBar2.png
│ │     │ │ ├── LowLifeBar3.png
│ │     │ │ ├── MediumLifeBar1.png
│ │     │ │ ├── MediumLifeBar2.png
│ │     │ │ ├── MoraleDown.png
│ │     │ │ └── MoraleUp.png
│ │     │ ├── races
│ │     │ │ ├── doctor.png
│ │     │ │ ├── dwarf.png
│ │     │ │ ├── elf.png
│ │     │ │ ├── lycanthrope.png
│ │     │ │ ├── orc.png
│ │     │ │ ├── reptilian.png
│ │     │ │ ├── vampire.png
│ │     │ │ ├── werebeast.png
│ │     │ │ └── zombie.png
│ │     │ ├── room
│ │     │ │ ├── Bed.png
│ │     │ │ ├── BedBlood.png
│ │     │ │ ├── BedBones.png
│ │     │ │ ├── BedCreature.png
│ │     │ │ ├── CloseDoor.png
│ │     │ │ └── DoctorInRoom.png
│ │     │ └── tiles
│ │     │     ├── FloorCrypt.png
│ │     │     ├── FloorOutside.png
│ │     │     ├── FloorQuarantine.jpg
│ │     │     └── HospitalBackground.jpg
│ │     └── logback.xml
│ └── test
│     └── java
│         └── com
│             ├── fantasyhospital
│             │ ├── controller
│             │ │ └── ui
│             │ │     └── ToolbarControllerTest.java
│             │ └── model
│             │     ├── creatures
│             │     │ ├── abstractclass
│             │     │ │ └── CreatureTest.java
│             │     │ └── DoctorTest.java
│             │     └── rooms
│             │         └── medicalservice
│             │             └── MedicalServiceTest.java
│             └── FantasyHospitalApplicationTest.java
└── tree.md

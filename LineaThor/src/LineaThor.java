import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.LightSensor;
import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.navigation.RotateMoveController;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;
import lejos.util.PilotProps;

public class LineaThor {

	final static int C7 = 2093;
	final static int B6 = 1975;
	final static int AIS6 = 1865;
	final static int A6 = 1760;
	final static int GIS6 = 1661;
	final static int G6 = 1568;
	final static int FIS6 = 1480;
	final static int F6 = 1397;
	final static int E6 = 1318;
	final static int DIS6 = 1244;
	final static int D6 = 1175;
	final static int CIS6 = 1109;
	final static int C6 = 1046;
	final static int B5 = 988;
	final static int AIS5 = 932;
	final static int A5 = 880;
	final static int GIS5 = 831;
	final static int G5 = 784;
	final static int FIS5 = 740;
	final static int F5 = 698;
	final static int E5 = 659;
	final static int DIS5 = 622;
	final static int D5 = 587;
	final static int CIS5 = 554;
	final static int C5 = 523;
	final static int BEAT = 250;
	final static int SLEEP = 100;
	final static int PAUSE = -1;
	final static int STARWARS_INTRO[][] = {
		{C5, 2*BEAT}, {F5, 4*BEAT},	{C6, 2*BEAT},
		{AIS5, BEAT}, {A5, BEAT},	{G5, BEAT},
		{F6, 4*BEAT}, {C6, 2*BEAT},	{AIS5, BEAT},
		{A5, BEAT},   {G5, BEAT},	{F6, 4*BEAT},
		{C6, 2*BEAT}, {AIS5, BEAT},	{A5, BEAT},
		{AIS5,BEAT},  {G5, 6*BEAT}, {PAUSE, 4*BEAT}
	};
	final static int JAWS[][] = {
		{D5, BEAT}, {DIS5, BEAT}, {D5, BEAT}, {E5, BEAT}
	};
	final static int BACKUP[][] = {
		{C6, BEAT}, {PAUSE,BEAT}, {C6, BEAT}, {PAUSE, BEAT}
	};
	
	public static void main (String[] aArg) throws Exception{
		
		Jukebox jukebox = new Jukebox();
		PilotProps pilotProps = new PilotProps();
		pilotProps.loadPersistentValues();
		float wheelDiameter = Float.parseFloat(pilotProps.getProperty(PilotProps.KEY_WHEELDIAMETER, "4.96"));
		float trackWidth = Float.parseFloat(pilotProps.getProperty(PilotProps.KEY_TRACKWIDTH, "13.0"));
		RegulatedMotor leftMotor = PilotProps.getMotor(pilotProps.getProperty(PilotProps.KEY_LEFTMOTOR, "B"));
		RegulatedMotor rightMotor = PilotProps.getMotor(pilotProps.getProperty(PilotProps.KEY_RIGHTMOTOR, "C"));
		boolean reverse = Boolean.parseBoolean(pilotProps.getProperty(PilotProps.KEY_REVERSE,"false"));
		
		final RotateMoveController pilot = new DifferentialPilot(wheelDiameter, trackWidth, leftMotor, rightMotor, reverse);
		final LightSensor lightSensor = new LightSensor(SensorPort.S1);
		final UltrasonicSensor ultrasonicSensor = new UltrasonicSensor(SensorPort.S2);
		ultrasonicSensor.continuous();
		pilot.setRotateSpeed(180);
		
		Behavior EnLinea = new Behavior() {
			
			public boolean takeControl() {
				return lightSensor.readValue() >= 56;
			}

			public void suppress() {
				pilot.stop();
			}
			public void action() {
				pilot.forward();
				while(lightSensor.readValue() >= 56) Thread.yield(); 
				
			}					
		};


		Behavior FueraLinea = new Behavior() {
			
			private boolean suppress = false;

			public boolean takeControl() {
				return lightSensor.readValue() < 56;
			}

			public void suppress() {
				suppress = true;
			}

			public void action() {
				int sweep = 10;
				while (!suppress) {
					pilot.rotate(sweep,true);
					while (!suppress && pilot.isMoving()) Thread.yield();
					sweep *= -2;
				}
				pilot.stop();
				suppress = false;
			}
		};

		Behavior pared = new Behavior() {


			public boolean takeControl() {
				
				return ultrasonicSensor.getDistance() < 10;
			}


			public void action() {
				pilot.stop();
				
			}


			public void suppress() {
				pilot.stop();
				
			}
		 };
		Behavior[] bArray = {FueraLinea, EnLinea, pared};
		LCD.drawString("Pulsa cualquier \nboton para \nempezar", 0, 1);
		Button.waitForAnyPress();
		
		jukebox.play(STARWARS_INTRO, false);
		(new Arbitrator(bArray)).start();
	}

}


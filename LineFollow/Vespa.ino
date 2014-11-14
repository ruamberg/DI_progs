// Vespa.ino

#define KP 1
#define KI 0
#define KD 0
#define CORRECTION(a,b,c) a+b+c
#define NUMSENSORS 10

int line[NUMSENSORS] = {3, 4, 5, 6, 7, 8, 9, 10, 11, 12};
//Para esquema 1, -9, -7, -5, -3, -1, 1, 3, 5, 7, 9; 
//Para esquema 2, 0, 10, 20, 30, 40, 50, 60, 70, 80, 90;
int error[NUMSENSORS] = {-9, -7, -5, -3, -1, 1, 3, 5, 7, 9};
int setPoint = 0; //Para esquema 1, setPoint = 0; Para esquema 2, setPoint = 45;
int currentPoint = 0;
int err = 0;
int iErr = 0, prevErr = 0;
int p = 0, i = 0, d = 0;
int extraSpeed = 0;
int baseSpeed = 0;
int maxSpeed = 0;
int blackLimit;
int timeFlag = 0;

void setup() {
	int i = 0;
	for(i = 0; i < NUMSENSORS; i++) {
		pinMode(line[i], INPUT);
	}
}

void loop() {
	err = getCurrentPoint() - setPoint;
	p = KP * err;
	i = KI * (iErr);
	d = KD * (err - prevErr);

	//Motor na velocidade base (baseSpeed) +- CORRECTION(p,i,d)
	rightMotor(baseSpeed - CORRECTION(p,i,d));
	leftMotor(baseSpeed + CORRECTION(p,i,d));

	accel(50, 5);

	prevErr = err;
	iErr += err;
}

int getCurrentPoint() {
	int i = 0;
	int numerador = 0, denominador = 0;
	for(i = 0; i < NUMSENSORS; i++) {
		if(analogRead(line[i]) > blackLimit) {
			denominador++;
			numerador += error[i];
		}
	} 
	if(denominador == 0) {
		denominador = 1;
	}

	return numerador/denominador;
}

void rightMotor(int speed) {
	int newSpeedRightMotor = speed;
	if(speed > 255) {
		newSpeedRightMotor = 255;
	}

	if(speed >= 0) {
		//liga o motor para frente com intensidade igual a newSpeedRightMotor
	}
	else {
		//liga o motor para trás com intensidade igual a -newSpeedRightMotor
	}
}

void leftMotor(int speed) {
	int newSpeedLeftMotor = speed;
	if(speed > 255) {
		newSpeedLeftMotor = 255;
	}

	if(speed >= 0) {
		//liga o motor para frente com intensidade igual a newSpeedLeftMotor
	}
	else {
		//liga o motor para trás com intensidade igual a -newSpeedLeftMotor
	}
}

void accel(int timeInterval, int accelFactor) {
	if(millis() - timeFlag >= timeInterval) {
		if(err == 0 && baseSpeed < maxSpeed)
			baseSpeed += accelFactor;
		else if(maxSpeed - baseSpeed < err) {
			baseSpeed -= accelFactor * 2;
		}
		timeFlag = millis();
	}
}

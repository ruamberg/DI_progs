// Vespa.ino
// Autor - Gabriel Alves de Lima
// Algoritmo de seguidor de linha
// Configuração utilizada para o arduino MEGA

//CONSTANTES DE CONTROLE NÃO CALIBRADAS
#define KP 1
#define KI 0
#define KD 0

//CONSTANTES DE AUXÍLIO
#define CORRECTION(a,b,c) a+b+c
#define NUMSENSORS 10

/******* CONFIGURAÇÃO PARA OS SENSORES *********/

	int line[NUMSENSORS] = {22, 24, 26, 28, 30, 32, 34, 36, 38, 40};
	//Para esquema 1, -9, -7, -5, -3, -1, 1, 3, 5, 7, 9; 
	//Para esquema 2, 0, 10, 20, 30, 40, 50, 60, 70, 80, 90;
	int error[NUMSENSORS] = {-9, -7, -5, -3, -1, 1, 3, 5, 7, 9};

/******** CONFIGURAÇÃO PARA OS MOTORES **********/

	int leftMotor[2] = {2, 3};
	int rightMotor[2] = {4, 5};

/******** CONFIGURAÇÃO PARA O CONTROLE **********/
	int currentPoint = 0;
	int setPoint = 0; //Para esquema 1, setPoint = 0; Para esquema 2, setPoint = 45;
	int err = 0;
	int iErr = 0, prevErr = 0;
	int p = 0, i = 0, d = 0;
	//Valores a serem testados
	int baseSpeed = 0;
	int maxSpeed = 0;
	unsigned long timeFlag = 0;

void setup() {
	//Definição das portas dos sensores como input e dos motores como output
	int i = 0;
	for(i = 0; i < NUMSENSORS; i++) {
		pinMode(line[i], INPUT);
	}

	for(i = 0; i < 2; i++) {
		pinMode(leftMotor[i], OUTPUT);
		pinMode(rightMotor[i], OUTPUT);
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

	accel(5, 50);

	prevErr = err;
	iErr += err;
}

int getCurrentPoint() {
	int i = 0;
	int numerador = 0, denominador = 0;
	for(i = 0; i < NUMSENSORS; i++) {
		if(digitalRead(line[i]); {
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
	if(speed > maxSpeed) {
		newSpeedRightMotor = maxSpeed;
	}

	if(speed >= 0) {
		analogWrite(rightMotor[0], newSpeedRightMotor);
		digitalWrite(rightMotor[1], LOW);
	}
	else {
		analogWrite(rightMotor[1], newSpeedRightMotor);
		digitalWrite(rightMotor[0], LOW);
	}
}

void leftMotor(int speed) {
	int newSpeedLeftMotor = speed;
	if(speed > maxSpeed) {
		newSpeedLeftMotor = maxSpeed;
	}

	if(speed >= 0) {
		analogWrite(leftMotor[0], newSpeedLeftMotor);
		digitalWrite(leftMotor[1], LOW);
	}
	else {
		analogWrite(leftMotor[1], newSpeedLeftMotor);
		digitalWrite(leftMotor[0], LOW);
	}
}

void accel(int accelFactor, int timeInterval) {
	if(millis() - timeFlag >= timeInterval) {
		if(err == 0 && baseSpeed < maxSpeed)
			baseSpeed += accelFactor;
		else if(maxSpeed - baseSpeed < err) {
			baseSpeed -= accelFactor * 2;
		}
		timeFlag = millis();
	}
}

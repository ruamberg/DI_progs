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
#define NUMSENSORS 11

/******* CONFIGURAÇÃO PARA OS SENSORES *********/

	//Valores que representam os sensores em ordem nas portas do arduino
	int sensor[NUMSENSORS] = {22, 24, 26, 28, 30, 32, 34, 36, 38, 40, 42};
	//Valores atribuídos para cara sensor em ordem (o peso de cada um)
	//Para esquema 1, -9, -7, -5, -3, -1, 1, 3, 5, 7, 9; 
	//Para esquema 2, 0, 10, 20, 30, 40, 50, 60, 70, 80, 90;
	int error[NUMSENSORS] = {-9, -7, -5, -3, -1, 0, 1, 3, 5, 7, 9};

/******** CONFIGURAÇÃO PARA OS MOTORES **********/
	
	//Portas do motor
	int leftMotor[2] = {2, 3};
	int rightMotor[2] = {4, 5};

/******** CONFIGURAÇÃO PARA O CONTROLE **********/
	
	int currentPoint = 0;  //Variável que vai armazenar o valor de posição atual do robô
	int setPoint = 0; //Para esquema 1, setPoint = 0; Para esquema 2, setPoint = 45;
	int err = 0; //Variável para armazenar o valor do erro
	int iErr = 0, prevErr = 0; //Respectivamente, erro integral e erro anterior
	int p = 0, i = 0, d = 0; //Valores p, i e d
	//Valores a serem testados
	int baseSpeed = 0;    //Velocidade base do robô
	int maxSpeed = 0;	  //Velocidade máxima atingível pelos motores
	unsigned long timeFlag = 0; //Sinal de tempo para aceleração

void setup() {
	//Definição das portas dos sensores como input e dos motores como output
	int i = 0;
	for(i = 0; i < NUMSENSORS; i++) {
		pinMode(sensor[i], INPUT); //Definição dos sensores como entrada
	}

	for(i = 0; i < 2; i++) {
		pinMode(leftMotor[i], OUTPUT); //Definição dos motores como saída
		pinMode(rightMotor[i], OUTPUT);
	}
}

void loop() {
	//Cálculo do erro: ponto atual subtraído do ponto de desejo
	err = getCurrentPoint() - setPoint;
	//Cálculo do P,I e D
	p = KP * err;
	i = KI * (iErr);
	d = KD * (err - prevErr);

	//Motor na velocidade base (baseSpeed) +- CORRECTION(p,i,d)
	rightMotor(baseSpeed - CORRECTION(p,i,d));
	leftMotor(baseSpeed + CORRECTION(p,i,d));

	//Aceleração ou desaceleração dependendo do erro
	accel(5, 50);

	//Atualiza o erro anterior e soma o erro no erro integral
	prevErr = err;
	iErr += err;

	//Previne windup (soma excessiva potencialmente perigosa do erro integral)
	preventWindup();
}

//Previne windup (soma excessiva potencialmente perigosa do erro integral)
void preventWindup() {
	if(iErr >= maxSpeed)
	{
		iErr = iErr/3;
	}
	else if(err == 0) {
		iErr = 0;
	}
}

//Cálculo do ponto atual do robô.
//Pela média dos pesos dos sensores na linha pela quantidade de sensores na linha
int getCurrentPoint() {
	int i = 0;
	int numerador = 0; //numerador: somatório dos pesos de cada sensor
	int denominador = 0; //denominador: quantidade de sensores na linha
	for(i = 0; i < NUMSENSORS; i++) {
		//Vê se o sensor está na linha branca
		if(!digitalRead(sensor[i]); {
			denominador++;
			numerador += error[i];
		}
	} 
	if(denominador == 0) {
		denominador = 1;
	}

	//Cálculo da média
	return numerador/denominador;
}

//Ativa o motor direito com intensidade 'speed'
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

//Ativa o motor esquerdo com intensidade 'speed'
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

//(Des)aceleração com em unidade v/ms²
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

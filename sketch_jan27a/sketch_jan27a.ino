
#include <math.h>
//bibliotecas para comunicação I2C com a Bússola HMC5883L
#include <Wire.h> 
#include <HMC5883L.h> 

int times = 0;
 
//controle de 2 DEBUGS dentro da programação
//o primeiro para o recebimento de dados e outro para a saída respectivamente
#define DEBUG0 0
#define DEBUG1 0

//pino do botão que liga ou desliga a thread t_pid
#define BUTTON 12

//máximo módulo recebido pelo vetor
#define MODULE_MAX 425

//'baudrate' da saída seria
#define SERIAL 9600

//número de repetições de leituras na bússola que serão feitas para se ter um valor de retorno
#define COMPASS_TIME 150

//define a potência mínima que o robô irá se mover
#define PERCENT_MIN 20

#define PID(a,b,c) a+b+c

//pinos dos motores - ponte H
int robot_out_0[2] = {3,4}, robot_out_1[2] = {5,6};
    
float angle_begin, error_angle, error_motor[2], error_velo, m_value; //float de conversão do angulo e dos errors do pid

float p_angle = 0, kp_angle = 1, i_angle = 0, ki_angle = 0;

double v_in[2] = {0,0}, v_robot[2] = {1,0}; //vetores de entrada e do robo

char x_axis[20] = {'0','\0'},  y_axis[20] = {'0','\0'};

boolean button; 

HMC5883L compass; //Instância a biblioteca para a bússola

void vector_in() 
{
//receber o vetor até o ponto
  char serial_in[20];
  char c = 0;
  byte index = 0;
  
//  double measure = millis();
  
  while(Serial.available() > 0 && c != '|') 
  {
    if(index < 19) 
    {
      c = Serial.read(); 
        x_axis[index] = c; 
        index++; 
        x_axis[index] = '\0';
      }
  }
  x_axis[index-1] = '\0';
    
  index = 0;
  while(Serial.available() > 0) 
  {
    if(index < 19) 
     {
       c = Serial.read(); 
       y_axis[index] = c; 
       index++; 
       y_axis[index] = '\0';
      }
   }
   
 //  measure = millis() - measure;
   
   Serial.println(times++);
  
  v_in[0] = atof(x_axis);
  v_in[1] = atof(y_axis); 
  
 /* 
  Serial.print(v_in[0]);
  Serial.print("|");
  Serial.println(v_in[1]);
 */
 
  m_value = sqrt(pow(v_in[0], 2) + pow(v_in[1], 2));

  if(m_value != 0)
  {
    v_in[0] /= m_value;
    v_in[1] /= m_value;
  }

   //receber o vetor do robô
   float angle;

   angle = angle_begin - get_angle();

   v_robot[0] = cos(angle);
   v_robot[1] = sin(angle);

   if(DEBUG0)
   {
    Serial.print(v_in[0]);
    Serial.print("|");
    Serial.print(v_in[1]);
    Serial.print("| Module: ");
    Serial.println(m_value);
    Serial.print(v_robot[0]);
    Serial.print("|");
    Serial.println(v_robot[1]);
   }
}

//função que recebe o ângulo da bússola
float get_angle()
{
  
  float heading; //Variável para armazenar o valor aferido
  float precisao; //Variável parar o melhorar a precisao do valor aferido

  precisao = 0; //Zera a variável para uma nova leitura

  for(int i=0;i<COMPASS_TIME;i++) //Faz a leitura 100 e armazenar a somatória
  {

    //Pega os dados necessários para o cálculo da bússola digital
    MagnetometerScaled scaled = compass.ReadScaledAxis();
    int MilliGauss_OnThe_XAxis = scaled.XAxis;
    heading = atan2(scaled.YAxis, scaled.XAxis);
    float declinationAngle = 0.3889; //Compensação dos polos (São Paulo - Recife)
    heading += declinationAngle;

    //Converte o valor aferido para angulo
    if(heading < 0)
    {
      heading += 2*PI;
    } 

    if(heading > 2*PI)
    {
      heading -= 2*PI;
    }  

    precisao = precisao + heading;
    delay(1);

  }

  heading = precisao/COMPASS_TIME; //Pega a somatória e tira a média dos valores aferidos
  return heading;
}

void pid()
{
  double produtoe[2], angle_b;
  float kp = 1;

  double moviment_sig;

  produtoe[0] = (v_in[0] * v_robot[0]) + (v_in[1] * v_robot[1]); //p.e. do angulo entre o vetor do robô e do vetor de posição recebido 
  produtoe[1] = (v_in[0] * (v_robot[0] * -1)) + (v_in[1] * (v_robot[1] * -1));//p.e. entre o vetor oposto ao robô e do vetor de posição recebido
  if(acos(produtoe[0]) <= acos(produtoe[1]))
  {
    angle_b = acos(produtoe[0]);
    moviment_sig = 1;
  }
  else
  {
    angle_b = acos(produtoe[1]);
    moviment_sig = -1;
  }

  if(v_in[0] != 0 || v_in[1] != 0) 
    error_angle = angle_b/PI;
  else
  {
    error_angle = 0;
    i_angle = 0;
  }

  if(v_robot[0] > v_in[0]) error_angle *= -1; //cálculo do erro para ser aplicado no motor, dependente do ângulo
  
  p_angle = error_angle * kp_angle;
  error_angle = PID(p_angle, i_angle, 0); //aplicação da proporcional  
  error_angle = (error_angle - (-1)) * (100 - (-100)) / (1 - (-1)) + (-100);

  error_velo = m_value; //cálculo do erro para ser aplicado no motor, dependente do módulo e da direção em relação ao eixo y
  error_velo *= kp;
  error_velo = (error_velo - 0) * ((255 - fabs(error_angle)) - 0) / (MODULE_MAX - 0);

  error_motor[0] = (error_velo + error_angle) * moviment_sig; //saída nos motores proporcionais ao erro
  error_motor[1] = (error_velo - error_angle) * moviment_sig;

  if(error_motor[0] < ((PERCENT_MIN/100.0)*255.0) && error_motor[0] > ((-PERCENT_MIN/100.0)*255.0))
    error_motor[0] = 0;
  if(error_motor[1] < ((PERCENT_MIN/100.0)*255.0) && error_motor[1] > ((-PERCENT_MIN/100.0)*255.0))
    error_motor[1] = 0;
  
  if(DEBUG1)
  {
    Serial.print("|");
    Serial.print(error_motor[0]);
    Serial.print("| . |");             //debug serial sobre a movimentação dos motores
    Serial.print(error_motor[1]);
    Serial.println("|");
  }

  moviment();

}

// função que executa a movimentação do robô
void moviment()
{
  //motor esquerda
  if(error_motor[0] > 0)
  {
    analogWrite(robot_out_0[0], fabs(error_motor[0]));
    digitalWrite(robot_out_0[1], LOW);
  }
  else if(error_motor[0] > 255)
  {
    analogWrite(robot_out_0[0], 255);
    digitalWrite(robot_out_0[1], LOW);  
  }
  else if(error_motor[0] < 0)
  {
    analogWrite(robot_out_0[1], fabs(error_motor[0]));
    digitalWrite(robot_out_0[0], LOW);
  }
  else if(error_motor[0] < -255)
  {
    analogWrite(robot_out_0[1], 255);
    digitalWrite(robot_out_0[0], LOW);
  }
  else
  {
    digitalWrite(robot_out_0[0], HIGH);
    digitalWrite(robot_out_0[1], HIGH);
  }

  //motor direita
  if(error_motor[0] > 0)
  {
    analogWrite(robot_out_1[0], fabs(error_motor[0]));
    digitalWrite(robot_out_1[1], LOW);
  }
  else if(error_motor[0] > 255)
  {
    analogWrite(robot_out_1[0], 255);
    digitalWrite(robot_out_1[1], LOW);  
  }
  else if(error_motor[0] < 0)
  {
    analogWrite(robot_out_1[1], fabs(error_motor[0]));
    digitalWrite(robot_out_1[0], LOW);
  }
  else if(error_motor[0] < -255)
  {
    analogWrite(robot_out_1[1], 255);
    digitalWrite(robot_out_1[0], LOW);
  }
  else
  {
    digitalWrite(robot_out_1[0], HIGH);
    digitalWrite(robot_out_1[1], HIGH);
  }
}


void setup()
{

  for(int x = 0; x < 2; x++)
  {
    pinMode(robot_out_0[x], OUTPUT);
    pinMode(robot_out_1[x], OUTPUT);
  }

  pinMode(BUTTON, INPUT);

  Serial.begin(SERIAL);

  Wire.begin(); //Inicia a comunicação o I2C

//Configura a bússola
  compass = HMC5883L();
  compass.SetScale(1.3);
  compass.SetMeasurementMode(Measurement_Continuous);
  
  angle_begin = get_angle();//seta o cangulo inicial do robo
}
  
void loop()
{
  button = digitalRead(BUTTON);
  double time_in_millis = millis();
  
  vector_in();
  pid();
  time_in_millis = millis() - time_in_millis;
  if(DEBUG0)
  {
    Serial.print("time:");
    Serial.println(time_in_millis);
  }
}


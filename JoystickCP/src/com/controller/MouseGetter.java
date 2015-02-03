package com.controller;

import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;

import javax.swing.JFrame;
import javax.swing.JPanel;


public class MouseGetter {
	
	//� um m�todo est�tico de retorno double[]. Os m�todos est�ticos n�o necessitam de
	//objetos para serem usados, necessitando apenas o nome da classe
	
	//m�todo que consegue os X e Y do mouse em um componente gr�fico (JFrame ou JPanel no caso)
	//Unidade: pixel
	public static double[] getMouseXY(JFrame si) {
		double[] result = new double[2];
		
		PointerInfo pointer = MouseInfo.getPointerInfo();
		Point a = pointer.getLocation();
		Point b = si.getLocation();
		
		result[0] = a.getX() - b.getX() - 10;
		result[1] = a.getY() - b.getY() - 30;
		
		return result;
	}	
	public static double[] getMouseXY(JPanel sip) {
		double[] result = new double[2];
		
		PointerInfo pointer = MouseInfo.getPointerInfo();
		Point a = pointer.getLocation();
		Point b = sip.getLocation();
		
		result[0] = a.getX() - b.getX();
		result[1] = a.getY() - b.getY();
		
		return result;
	}
	//m�todo n�o utilizado. S� retorna se o mouse est� dentro de um JFrame fornecido
	public static boolean isInside(JFrame si) {
		double[] xy = getMouseXY(si);
		if((xy[0] > 0 && xy[0] < 640) && (xy[1] > 0 && xy[1] < 480)) {
			return true;
		}
		else {
			return false;
		}
	}
}

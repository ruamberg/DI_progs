/*
 * Teste da Clsse MovimentoConta e seus m�todos
 */

package com.acme.testes.conta;

import java.util.Calendar;

import com.acme.rn.cliente.*;
import com.acme.rn.conta.*;

public class TesteMovimentoConta {
	public static void main(String[] args) {
		
		//Cria a conta de Origem
		ContaMilhagem contaOrigem = new ContaMilhagem(new IdentificadorConta(4556), 
									new Cliente(new Cpf("12345678900"), "Jo�o Correia Alves", 56, 
										10987.23, Cliente.MASCULINO));

		//Instancia o objeto do tipo Calendar para passar como par�metro de Date
		Calendar c = Calendar.getInstance();
		
		//Cria o movimento
		MovimentoConta movimentoConta = new MovimentoConta(contaOrigem, 500, "NomeDaFonte", c.getTime());
		
		//Usa o m�todo getNomeExtrato()
		System.out.println("Nome Extrato: \n" + movimentoConta.getNomeExtrato());
		//Usa o m�todo toString()
		System.out.println("Resumo: " + movimentoConta.toString());
		//Usa o m�todo getChave()
		System.out.println("Chave: " + movimentoConta.getChave());
	}
}

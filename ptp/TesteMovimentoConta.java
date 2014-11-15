/*
 * Teste da Clsse MovimentoConta e seus métodos
 */

package com.acme.testes.conta;

import java.util.Calendar;

import com.acme.rn.cliente.*;
import com.acme.rn.conta.*;

public class TesteMovimentoConta {
	public static void main(String[] args) {
		
		//Cria a conta de Origem
		ContaMilhagem contaOrigem = new ContaMilhagem(new IdentificadorConta(4556), 
									new Cliente(new Cpf("12345678900"), "João Correia Alves", 56, 
										10987.23, Cliente.MASCULINO));

		//Instancia o objeto do tipo Calendar para passar como parâmetro de Date
		Calendar c = Calendar.getInstance();
		
		//Cria o movimento
		MovimentoConta movimentoConta = new MovimentoConta(contaOrigem, 500, "NomeDaFonte", c.getTime());
		
		//Usa o método getNomeExtrato()
		System.out.println("Nome Extrato: \n" + movimentoConta.getNomeExtrato());
		//Usa o método toString()
		System.out.println("Resumo: " + movimentoConta.toString());
		//Usa o método getChave()
		System.out.println("Chave: " + movimentoConta.getChave());
	}
}

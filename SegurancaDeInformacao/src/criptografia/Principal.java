package criptografia;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;


public class Principal {
	public static void main(String[] args) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException, UnsupportedEncodingException {

		/*
		//Criar um chave automagicamente no sistema de 16 e 32 caracteres. 
		Scanner leitor = new Scanner(System.in);
		System.out.println("Deseja inserir uma chave de 16 ou 32 caracteres?" );
		int opcao = leitor.nextInt();
		String chave = "";
		if(opcao == 8) {
			chave = getRandomString(16);
			System.out.println(chave);
		} else if( opcao == 16) {
			chave = getRandomString(32);
			System.out.println(chave);
		} else {
			System.out.println("Valor invalido. ");
		}*/
		
		//Inicia conexão com meu servidor de chaves
		KDC kdc = new KDC();
		
		
		//Adiciona o 1º contato para iniciar um chat.
		Usuario usuarioA = new Usuario();
		usuarioA.setNome("Bob");	
		usuarioA.setKeyMestra(GerarChave.getRandomString(16));
		//Criptografa meu nome com chave aleatorio para realizar autenticação no KDC
		byte[] autenticacao1 = AES.cifra(usuarioA.getNome(), usuarioA.getKeyMestra());
		kdc.chaves.add(usuarioA.getNome()+";"+usuarioA.getKeyMestra());
		
		//Adiciona o 2º contato para iniciar um chat.
		Usuario usuarioB = new Usuario();
		usuarioB.setNome("Alice");	
		usuarioB.setKeyMestra(GerarChave.getRandomString(16));
		//Criptografa meu nome com chave aleatorio para realizar autenticação no KDC
		byte[] autenticacao2 = AES.cifra(usuarioB.getNome(), usuarioB.getKeyMestra());
		kdc.chaves.add(usuarioB.getNome()+";"+usuarioB.getKeyMestra());
		
		//SIMULAR CONVERSA ENTRE BOB E ALICE
		System.out.println("Iniciando uma conversa entre BOB e ALICE");
		
		//Criptografo o usuario que quero entrar em contato com minha chave
		byte[] nomeContato = AES.cifra(usuarioB.getNome(), usuarioA.getKeyMestra());
		
		//Chama metodo para realizar autenticação no servidor e trazer chaves para comunicação
		List<byte[]> retornos = kdc.doReceberAndAutenticarChave(usuarioA.getNome(), autenticacao1, nomeContato);
		
		//Bob recebe 2 chaves 
		//Bob decifra sua chave de sessão
		String sessao = usuarioA.getKeyDaSessao(retornos.get(0), usuarioA.getKeyMestra());
		usuarioA.setKeySessao(sessao);
		System.out.println("Chave de sessão na K_BOB: "+usuarioA.getKeySessao());
		
		//Bob envia a Alice uma chave de sessão criptografada na chave da alice.
		System.out.println("-----");
		System.out.println("Bob envia o paramentro da chave para a Alice");
		System.out.println("-----");
		String chaveSessaoRecebida = usuarioB.getKeyDaSessao(retornos.get(1), usuarioB.getKeyMestra());
		usuarioB.setKeySessao(chaveSessaoRecebida);
		System.out.println("Chave de sessão na K_ALICE: "+usuarioA.getKeySessao());
		
		//Gera um nounce no sistema para fazer envio e tratar a função - para efeito de testo o nounce sera gerado de forma manual
		//Nounce é criptografado com a chave da sessão e enviada ao bob
		byte[] nounce = AES.cifra("123456", usuarioB.getKeySessao());
		usuarioB.setNounce(nounce);
		System.out.println("Nouce é enviado para BOB");
		
		//Bob recebe um nouce e decifra ele com a chave de sessão, executa a função e envia para a alice validar os resultados;
		//Bob decifra o nounce
		//Bob realiza fução do nounce
		String retornoNounce = usuarioA.decifraNounce(nounce, usuarioA.getKeySessao());
		String newNounce = usuarioA.realizarFuncaoNounce(retornoNounce);
		byte[] newNounceEnviar = AES.cifra(newNounce, usuarioB.getKeySessao());
		System.out.println("Função foi aplicada ao nounce e será enviada para Alice.");
		System.out.println("Alice esta validando os dados");
		//Alice recebe o NEWNOUNCE que bob executou na função e faz sua validação para fechar a comunicação
		String retorno = usuarioB.validaNEWNOUCE(newNounceEnviar, usuarioB.getNounce(), usuarioB.getKeySessao());
		System.out.println(retorno);
		System.out.println("NEWNOUNCE Validado - COMUNICAÇÂO ESTABELECIDA");
	}
	

}

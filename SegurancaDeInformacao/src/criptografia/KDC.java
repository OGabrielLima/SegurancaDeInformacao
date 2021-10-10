package criptografia;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class KDC{
	ArrayList<String> chaves = new ArrayList();
	
	public List<byte[]> doReceberAndAutenticarChave(String remetente, byte[] textoCriptografado, byte[] Destinatario) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException, UnsupportedEncodingException {
		//Obtem a chave do remetente
		String chaveRemetente = getChaveUsuario(remetente);
		String chaveDestinatario = "";
		
		String remetenteLimpo = AES.decifra(textoCriptografado, chaveRemetente);
		String destinatarioLimpo = AES.decifra(Destinatario, chaveRemetente);
		
		String chaveSessao = "";
		
		if(remetente.equals(remetenteLimpo)) {
			System.out.println("Usuario validado na KDC: "+remetente);
			//Gera chave de sessão para o remetente
			chaveSessao = GerarChave.getRandomString(16);
			
			//Busca chave do destinatario
			chaveDestinatario = getChaveUsuario(destinatarioLimpo);
			
		}
		List<byte[]> retorno = new LinkedList<byte[]>();
				
		byte[] retorno1 = AES.cifra(chaveSessao, chaveRemetente);
		byte[] retorno2 = AES.cifra(chaveSessao, chaveDestinatario); 
		retorno.add(retorno1);
		retorno.add(retorno2);
		
		System.out.println("Devolvendo 2 parametros criptografados");
		
		return retorno;
		
	}
	
	public String getChaveUsuario(String nome) {
		String chave = "";
		//Busca chave do destinatario a partir do nome da parte que foi descriptografado
		for(int i=0; i< chaves.size(); i++) {
			if(chaves.get(i).contains(nome)) {
				String key = chaves.get(i).toString();
				//System.out.println(key);
				String[] retornoSplit = key.split(";");
				chave = retornoSplit[1];
				//System.out.println(chave);
			}
		}
		return chave;
	}

	public ArrayList<String> getChaves() {
		return chaves;
	}

	public void setChaves(ArrayList<String> chaves) {
		this.chaves = chaves;
	}
	
	
	
	
}

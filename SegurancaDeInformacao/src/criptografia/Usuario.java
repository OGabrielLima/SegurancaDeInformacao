package criptografia;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class Usuario {
	private String nome;
	private String KeyMestra;
	private String KeySessao;
	private String mensagem;
	private byte[] retornoChave;
	private byte[] nounce;
	
	public String realizarFuncaoNounce(String nounce){
		int nounceInt = Integer.parseInt(nounce);
		int newNounce = nounceInt++;
		String retorno = newNounce + "";
		return retorno;
	}

	public String getKeyDaSessao(byte[] retorno, String chave) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException, UnsupportedEncodingException{
		String chaveSessao = AES.decifra(retorno, chave);
		return chaveSessao;
	}
	
	public String decifraNounce(byte[] nounce, String chave) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException, UnsupportedEncodingException{
		String retorno = AES.decifra(nounce, chave);
		return retorno;
	}
	
	public String validaNEWNOUCE(byte[] nounce, byte[] meuNounce, String chave) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException, UnsupportedEncodingException {
		String nounce1 = decifraNounce(nounce, chave);
		String nounce2 = decifraNounce(meuNounce, chave);
		if(nounce1.equals(nounce2)) {
			return "Valores validados com sucesso!!!";
		}
		return "Erro ao validar Nounces. Favor verificar novamente o processo";
		
	}
	
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getKeyMestra() {
		return KeyMestra;
	}
	public void setKeyMestra(String keyMestra) {
		KeyMestra = keyMestra;
	}
	public String getKeySessao() {
		return KeySessao;
	}
	public void setKeySessao(String keySessao) {
		KeySessao = keySessao;
	}
	public String getMensagem() {
		return mensagem;
	}
	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}
	public byte[] getRetornoChave() {
		return retornoChave;
	}
	public void setRetornoChave(byte[] retornoChave) {
		this.retornoChave = retornoChave;
	}
	public byte[] getNounce() {
		return nounce;
	}
	public void setNounce(byte[] nounce) {
		this.nounce = nounce;
	}
	
	
	
}

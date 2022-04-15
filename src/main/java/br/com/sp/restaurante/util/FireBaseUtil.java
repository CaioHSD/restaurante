package br.com.sp.restaurante.util;

import java.io.IOException;
import java.util.UUID;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

//classe que o spring reconhece que tem uma lógica de spring boot
@Service
public class FireBaseUtil {
	// variável para guardar as credenciais de acesso
	private Credentials credenciais;
	
	//variável para acessar e manipular o storage
	private Storage storage;
	
	//constante para i nime do bucket
	private final String BUCKET_NAME="restaguide-caio.appspot.com";
	
	//constante prefixo da URL
	private final String PREFIX = "https://firebasestorage.googleapis.com/v0/b/" +BUCKET_NAME+ "/o/";
	
	//constante o sufixo da URL
	private final String SUFFIX = "?alt=media";
	
	//constante para a URL
	private final String DOWNLOAD_URL = PREFIX + "%s" + SUFFIX;
	
	public FireBaseUtil() {
		//acessa o arquivo json com a chave
		Resource resource = new ClassPathResource("chave-firebase.json");
		//gera uma credencial no Firebase através da chave do aquivo
		try {
			credenciais = GoogleCredentials.fromStream(resource.getInputStream());
			
			//cria o storage para manipular os dadoes no Firebase
			storage = StorageOptions.newBuilder().setCredentials(credenciais).build().getService();
			
			
			
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		
		}catch (Exception e ) {
			e.printStackTrace();
		}
		
		
	}
	
	//método para extrair a extensão do arquivo
	private String getExtensao(String nomeArquivo) {
		//extrai o ponto do arquivo onde está a extensão
		return nomeArquivo.substring(nomeArquivo.lastIndexOf('.'));
		
	}
	
	//método que faz o upload
	public String upload(MultipartFile arquivo) throws IOException {
		//gera um nome aleatório para o arquivo
		String nomeArquivo = UUID.randomUUID().toString() + getExtensao(arquivo.getOriginalFilename());
		
		// criar um blobId através do nome gerado para o arquivo
		BlobId blobId = BlobId.of(BUCKET_NAME, nomeArquivo);
		
		//criar um blobinfo atrasvés do BlobId
		BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType("media").build();
		
	//grava o blobinfo no Storage passando os bytes do arquivo 
		storage.create(blobInfo, arquivo.getBytes());
		
		
		//retorna a URL do arquivo gerado no Storage
		return String.format(DOWNLOAD_URL, nomeArquivo);
		
		
	}
	//método que exclui o arquivo do storage
	public void deletar(String nomeArquivo) {
		//retirar o prefixo e o sufixo da string
		nomeArquivo = nomeArquivo.replace(PREFIX, "").replace(SUFFIX, "");
		//obter um Blob através do nome
		Blob blob = storage.get(BlobId.of(BUCKET_NAME, nomeArquivo));
		//deleta através do blob
		storage.delete(blob.getBlobId());
		
		
	}
	
	
	
	
}

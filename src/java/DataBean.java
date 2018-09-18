/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ApplicationScoped;
import java.net.Socket;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

/**
 *
 * @author hfischer
 */

@ManagedBean
@ApplicationScoped
public class DataBean {

    private Socket clientSocket = null;
    private String node = null;
    private String port = null;
    
    /**
     * Creates a new instance of DataBean
     */
    public DataBean() {
    }
    
    public void setClientSocket(Socket socket){
        this.clientSocket = socket;
    }
    
    public Socket getClientSocket(){
        return this.clientSocket;
    }
    
    public void setNode(String node){
        this.node = node;
    }
    
    public String getNode(){
        return this.node;
    }
    
    public void setPort(String port){
        this.port = port;
    }
    
    public String getPort(){
        return this.port;
    }
    
    @PostConstruct
    public void init(){
        readFromFile();
        
    }
    
    @PreDestroy
    public void done(){
        writeToFile();
    }
    
    public void connect(String val){
        showInfoMessage("Connect");
    }
    
    public void openSocket(){
       
        try{
        if (clientSocket!=null) {
            clientSocket.close();
            clientSocket = null;
        }
        clientSocket = new Socket(this.node,Integer.parseInt(this.port));
       }
       catch(Exception e){
           showErrMessage(e.getMessage());
       }
    }
    
    public void closeSocket(){
        try{
            if (clientSocket != null){
                clientSocket.close();
                clientSocket = null;
            }
        }
        catch (Exception e){
            
        }
    }
    
    public void readFromFile(){
        
    }
    
    public void writeToFile(){
        
    }
    
    public void showInfoMessage(String msg){
        FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_INFO,"Info",msg));
    }
    
    public void showWarnMessage(String msg){
        FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_WARN,"Warning",msg));
    }
    
    public void showErrMessage(String msg){
        FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR,"Error",msg));
    }
}

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
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import java.util.regex.*;
import java.io.*;
import java.util.Properties;
import javax.faces.context.ExternalContext;
/**
 *
 * @author hfischer
 */

@ManagedBean(name = "data")
@ApplicationScoped
public class DataBean {

    private Socket clientSocket = null;
    @ManagedProperty(value = "#{node}")
    private String node = "192.168.0.12";
    @ManagedProperty(value = "#{port}")
    private String port = "2048";
    
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
        showInfoMessage(val);
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
    
    public boolean readFromFile(){
        boolean success = false;
        BufferedReader buffReader = null;
        try{
           ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
           Properties properties = new Properties();
           properties.load(externalContext.getResourceAsStream("/WEB-INF/DMXControl.cfg"));
           node = properties.getProperty("Node");
           port = properties.getProperty("Port");
           success = true;
        }
        catch (Exception e){
            showErrMessage(e.getMessage() + "\r\n" + "Loading Defaults");
            node = "192.168.0.208";
            port = "4096";
            success = false;
        }
        finally{
            try{
                if (buffReader!=null) buffReader.close();
                buffReader = null;
            }
            catch (IOException e){
                
            }
        }
        return success;
    }
    
    public void writeToFile(){
        OutputStream os = null;
        try{
           Properties properties = new Properties();
           properties.setProperty("Node", node);
           properties.setProperty("Port", port);
           ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
           os = new FileOutputStream(context.getRealPath("/WEB-INF/DMXControl.cfg"));         
           properties.store(os, "DMXControl Data");            
        }
        catch (IOException e){
            showErrMessage(e.getMessage());
        }
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

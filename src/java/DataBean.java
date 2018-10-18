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
import java.util.*;
import java.io.*;
import java.util.Properties;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.validation.constraints.*;
import javax.faces.event.ValueChangeEvent;


/**
 *
 * @author hfischer
 */

@ManagedBean(name = "data")
@ApplicationScoped
public class DataBean {

    private Socket clientSocket = null;
    @ManagedProperty(value = "#{node}")
    @Pattern(regexp = "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])$", message = "Node Value must be a valid IP Address")
    private String node = "192.168.0.12";
    @ManagedProperty(value = "#{port}")
    @Pattern(regexp = "^((6553[0-5])|(655[0-2][0-9])|(65[0-4][0-9]{2})|(6[0-4][0-9]{3})|([1-5][0-9]{4})|([0-5]{0,5})|([0-9]{1,4}))$", message = "Port Value must be 0 - 65535")
    private String port = "2048";
    @ManagedProperty(value = "#{channel0}")
    private int channel0;    
    @ManagedProperty("#{channel1}")
    private int channel1;
    
    /**
     * Creates a new instance of DataBean
     */
    public DataBean() {
    }
    
    public void setChannel0(int component){
        this.channel0 = component;
    }
    
    public int getChannel0(){
        return this.channel0;
    }
    
    public void setChannel1(int component){
        this.channel1 = component;
    }
    
    public int getChannel1(){
        return this.channel1;
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
        connect();
        channel0 = 0;
        channel1 = 0;
    }
    
    @PreDestroy
    public void done(){
        writeToFile();
        closeSocket();
    }
    
    public void connect(){
        showInfoMessage("Opening Socket:" + "Node:" + node + " Port:" + port);
        openSocket();
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
        
    public void onFlash(int what){
        showInfoMessage("OnFlash" + what);
    }
    
    public void onSliderEvent(){
        
        FacesContext fc = FacesContext.getCurrentInstance();
        Map<String, String> params = fc.getExternalContext().getRequestParameterMap();
        String param1 = params.get("What");
        
        showInfoMessage("Channel0 :" + channel0 + " Id: " + param1);
        //if (what == 0) channel0 = component;
        //if (what == 1) channel1 = component;
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

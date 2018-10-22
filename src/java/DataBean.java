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
    @ManagedProperty(value = "#{channel1}")
    private int channel1;
    @ManagedProperty(value = "#{channel2}")
    private int channel2;
    @ManagedProperty(value = "#{channel3}")
    private int channel3;
    @ManagedProperty(value = "#{channel4}")
    private int channel4;
    @ManagedProperty(value = "#{channel5}")
    private int channel5;
    @ManagedProperty(value = "#{channel6}")
    private int channel6;
    @ManagedProperty(value = "#{channel7}")
    private int channel7;
    @ManagedProperty(value = "#{channelMaster}")
    private int channelMaster;
    
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

    public void setChannel2(int component){
        this.channel2 = component;
    }
    
    public int getChannel2(){
        return this.channel2;
    }
    
    public void setChannel3(int component){
        this.channel3 = component;
    }

    public int getChannel3(){
        return this.channel3;
    }

    public void setChannel4(int component){
        this.channel4 = component;
    }
    
    public int getChannel4(){
        return this.channel4;
    }


    public void setChannel5(int component){
        this.channel5 = component;
    }

    public int getChannel5(){
        return this.channel5;
    }
    
    public void setChannel6(int component){
        this.channel6 = component;
    }

    public int getChannel6(){
        return this.channel6;
    }
    
    public void setChannel7(int component){
        this.channel7 = component;
    }
    
    public int getChannel7(){
        return this.channel7;
    }
    
    public void setChannelMaster(int component){
        this.channelMaster = component;
    }
    
    public int getChannelMaster(){
        return this.channelMaster;
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
        if (readFromFile() != true)
            loadDefaults();
        connect();
    }
    
    @PreDestroy
    public void done(){
        writeToFile();
        closeSocket();
    }
    
    public void loadDefaults(){
        node = "192.168.0.204";
        port = "2048";
        channel0 = 0;
        channel1 = 0;
        channel2 = 0;
        channel3 = 0;
        channel4 = 0;
        channel5 = 0;
        channel6 = 0;
        channel7 = 0;
        channelMaster = 0;        
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
        what +=1;
        showInfoMessage("OnFlash: " + what);
    }
    
    public void onSliderEvent(){
        
        FacesContext fc = FacesContext.getCurrentInstance();
        Map<String, String> params = fc.getExternalContext().getRequestParameterMap();
        String param1 = params.get("Channel 1");
        if (param1 != null){ 
            showInfoMessage("Channel 1: " + param1);
            channel0 = Integer.parseInt(param1);
        }
        param1 = params.get("Channel 2");
        if (param1 != null){
            showInfoMessage("Channel 2: " + param1);
            channel1 = Integer.parseInt(param1);
        }
        param1 = params.get("Channel 3");
        if (param1 != null){
            showInfoMessage("Channel 3: " + param1);
            channel2 = Integer.parseInt(param1);
        }
        param1 = params.get("Channel 4");
        if (param1 != null){
            showInfoMessage("Channel 4: " + param1);
            channel3 = Integer.parseInt(param1);            
        }
        param1 = params.get("Channel 5");
        if (param1 != null){
            showInfoMessage("Channel 5: " + param1);
            channel4 = Integer.parseInt(param1);            
        }
        param1 = params.get("Channel 6");
        if (param1 != null){
            showInfoMessage("Channel 6: " + param1);
            channel5 = Integer.parseInt(param1);            
        }
        param1 = params.get("Channel 7");
        if (param1 != null){
            showInfoMessage("Channel 7: " + param1);
            channel6 = Integer.parseInt(param1);            
        }
        param1 = params.get("Channel 8");
        if (param1 != null){
            showInfoMessage("Channel 8: " + param1);
            channel7 = Integer.parseInt(param1);            
        }
        param1 = params.get("Master");
        if (param1 != null){
            showInfoMessage("Master: " + param1);
            channelMaster = Integer.parseInt(param1);            
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
           channel0 = Integer.parseInt(properties.getProperty("Channel0"));
           channel1 = Integer.parseInt(properties.getProperty("Channel1"));
           channel2 = Integer.parseInt(properties.getProperty("Channel2"));
           channel3 = Integer.parseInt(properties.getProperty("Channel3"));
           channel4 = Integer.parseInt(properties.getProperty("Channel4"));
           channel5 = Integer.parseInt(properties.getProperty("Channel5"));
           channel6 = Integer.parseInt(properties.getProperty("Channel6"));
           channel7 = Integer.parseInt(properties.getProperty("Channel7"));
           channelMaster = Integer.parseInt(properties.getProperty("ChannelMaster"));
           success = true;
        }
        catch (Exception e){
            showErrMessage(e.getMessage() + "\r\n" + "Loading Defaults");
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
           properties.setProperty("Channel0", Integer.toString(channel0));
           properties.setProperty("Channel1", Integer.toString(channel1));
           properties.setProperty("Channel2", Integer.toString(channel2));
           properties.setProperty("Channel3", Integer.toString(channel3));
           properties.setProperty("Channel4", Integer.toString(channel4));
           properties.setProperty("Channel5", Integer.toString(channel5));
           properties.setProperty("Channel6", Integer.toString(channel6));
           properties.setProperty("Channel7", Integer.toString(channel7));
           properties.setProperty("ChannelMaster", Integer.toString(channelMaster));
           ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
           os = new FileOutputStream(context.getRealPath("/WEB-INF/DMXControl.cfg"));         
           properties.store(os, "DMXControl Data");            
        }
        catch (IOException e){
            showErrMessage(e.getMessage());
        }
        finally{
            try{
                if (os != null){
                    os.close();
                    os = null;
                }
            }
            catch (Exception e){
                
            }
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

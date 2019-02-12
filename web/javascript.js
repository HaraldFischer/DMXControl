/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

function onSlide(event){
    var component = event.getComponent();
    if (component !== null){
        alert(component);
    }
    else alert("Null");

}

function onBind(){
    var $handleEvent = jQuery('#idbtn');
    $handleEvent.on('click', onBind);

}

function checkValue(val){
    if (val > 100) return 100;
    else if (val<0) return 0;
    return val;
}

function showChanges(changes){
    
}
package com.yyide.doorcontrol.mina;

/**
 * Created by Administrator on 2019/4/4.
 */

public class MinaEventType {

   public int i;
   public String  id;
   public MinaEventType(){

   }

   public MinaEventType(int i){
      this.i=i;
   }

   public MinaEventType(int i,String id){
      this.i=i;
      this.id=id;
   }

}
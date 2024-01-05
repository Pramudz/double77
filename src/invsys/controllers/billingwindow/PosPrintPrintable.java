package invsys.controllers.billingwindow;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;

import invsys.entities.Company;
import invsys.entities.SalesDetails;
import javafx.collections.ObservableList;


// this is not used at all copied from third party
public class PosPrintPrintable implements Printable {

	
	private ObservableList<SalesDetails> detailList;

	private Company companyDetails;
	
	
	
	public PosPrintPrintable(ObservableList<SalesDetails> salesDetailList, Company companyDetails) {
		this.detailList = salesDetailList;
		this.companyDetails = companyDetails;
		
	}
	
	
	 public int print(Graphics graphics, PageFormat pageFormat,int pageIndex)  throws PrinterException 
			  {    
			     	                
			        
			      int result = NO_SUCH_PAGE;    
			        if (pageIndex == 0) {                    
			        
			            Graphics2D g2d = (Graphics2D) graphics;                    

			            double width = pageFormat.getImageableWidth();                    
			           
			            g2d.translate((int) pageFormat.getImageableX(),(int) pageFormat.getImageableY()); 

			            ////////// code by alqama//////////////

			            FontMetrics metrics=g2d.getFontMetrics(new Font("Arial",Font.BOLD,7));
			        //    int idLength=metrics.stringWidth("000000");
			            //int idLength=metrics.stringWidth("00");
			            int idLength=metrics.stringWidth("0000");
			            int amtLength=metrics.stringWidth("000000");
			            int qtyLength=metrics.stringWidth("00000");
			            int priceLength=metrics.stringWidth("000000");
			            int prodLength=(int)width - idLength - amtLength - qtyLength - priceLength-17;

			        //    int idPosition=0;
			        //    int productPosition=idPosition + idLength + 2;
			        //    int pricePosition=productPosition + prodLength +10;
			        //    int qtyPosition=pricePosition + priceLength + 2;
			        //    int amtPosition=qtyPosition + qtyLength + 2;
			            
			            int productPosition = 0;
			            int discountPosition= prodLength+5;
			            int pricePosition = discountPosition +idLength+10;
			            int qtyPosition=pricePosition + priceLength + 4;
			            int amtPosition=qtyPosition + qtyLength;
			            
			            
			              
			        try{
			            /*Draw Header*/
			            int y=20;
			            int yShift = 10;
			            int headerRectHeight=15;
			            int headerRectHeighta=40;
			            
			            ///////////////// Product names Get ///////////
			                String  pn1a="sss";
			                String pn2a="sss";;
			                String pn3a="sss";
			                String pn4a="sss";
			            ///////////////// Product names Get ///////////
			                
			            
			            ///////////////// Product price Get ///////////
			                int pp1a=1;
			                int pp2a=1;
			                int pp3a=1;
			                int pp4a=1;
			                int sum=pp1a+pp2a+pp3a+pp4a;
			            ///////////////// Product price Get ///////////
			                
			            g2d.setFont(new Font("Monospaced",Font.PLAIN,9));
			            
			            g2d.drawString("-------------------------------------",12,y);y+=yShift;
			            g2d.drawString("      Restaurant Bill Receipt        ",12,y);y+=yShift;
			            g2d.drawString("-------------------------------------",12,y);y+=headerRectHeight;
			      
			            g2d.drawString("-------------------------------------",10,y);y+=yShift;
			            g2d.drawString(" Food Name                 T.Price   ",10,y);y+=yShift;
			            g2d.drawString("-------------------------------------",10,y);y+=headerRectHeight;
			            g2d.drawString(" "+pn1a+"                  "+pp1a+"  ",10,y);y+=yShift;
			            g2d.drawString(" "+pn2a+"                  "+pp2a+"  ",10,y);y+=yShift;
			            g2d.drawString(" "+pn3a+"                  "+pp3a+"  ",10,y);y+=yShift;
			            g2d.drawString(" "+pn4a+"                  "+pp4a+"  ",10,y);y+=yShift;
			            g2d.drawString("-------------------------------------",10,y);y+=yShift;
			            g2d.drawString(" Total amount: "+sum+"               ",10,y);y+=yShift;
			            g2d.drawString("-------------------------------------",10,y);y+=yShift;
			            g2d.drawString("          Free Home Delivery         ",10,y);y+=yShift;
			            g2d.drawString("             03111111111             ",10,y);y+=yShift;
			            g2d.drawString("*************************************",10,y);y+=yShift;
			            g2d.drawString("    THANKS TO VISIT OUR RESTUARANT   ",10,y);y+=yShift;
			            g2d.drawString("*************************************",10,y);y+=yShift;
			                   
			           
			             
			           
			            
//			            g2d.setFont(new Font("Monospaced",Font.BOLD,10));
//			            g2d.drawString("Customer Shopping Invoice", 30,y);y+=yShift; 
			          

			    }
			    catch(Exception r){
			    r.printStackTrace();
			    }

			              result = PAGE_EXISTS;    
			          }    
			          return result;    
			      }


			}


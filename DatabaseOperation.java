package autoposter;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.ObjectInputStream.GetField;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.Message;
import javax.mail.MessagingException;

public class DatabaseOperation {

	static boolean flag=true;
	//data members for database connection
	Connection conn;
	final String DB_URL="jdbc:mysql://bo.favista.in/favista";
	final String User_Name="poster";
	final String Password="KKafahf@dsgad";
	//data members for get the listings
	static int site_id;
	static String pID;
	static String username;
	static String password;
	static String postingStatus;
	static int uid;
	private int queryTimeout=10;
    //data members for get project details
	
	static String pjID;
	
	static int propClassId;

	static int location_id;

	static int price;

	static int bedrooms;
	
	static int bathrooms;
	
	static String myTitle;

	static String age;

	static int possessionYear;

	static int balcony;

	static int study;

	static int servantRoom;
	
	static int newOrResale;

	static int bArea;
	
	static String furnishing;

	static String facing;

	static Date possession_date;
	
	static String phase;
	
	static String project_name;
	
	static String reserved_parking ;
	
	static String visitors_parking ;
	
	static String garbage;
	
	static String water_supply;
	
	static String swimming_pool;
	
	static String gym ;
	
	static String kids_area;
	
	static String party_area ;
	
	static String water_treatment;
	
	static String park;
	
	static String sewage_treatment;
	
	static String atm;
	
		//Method for sending email
	public static void sendmail(String msg){
		//sending email to Suraj Kumar Mishra ,Swaroop Kumar ,Abhimanyu Kumar and Manila
		System.out.println(msg + ". Sending email from  poster.favista@gmail.com...");
		final String from_uname = "poster.favista@gmail.com";
		final String from_password = "poster!@#$";
	    final String to_emailIds[]={"manila.k@favista.com","suraj.kumar@favista.com","swaroop@favista.com","abhimanyu.kumar@favista.com"};
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");

		Session session = Session.getInstance(props,
		new javax.mail.Authenticator() {
		protected PasswordAuthentication getPasswordAuthentication() {
			return new PasswordAuthentication(from_uname, from_password);
			}
		});

		try {
			for(int i=0;i<1;i++){
			MimeMessage message = new MimeMessage(session);
		    message.setFrom(new InternetAddress("poster.favista@gmail.com"));
			message.setRecipients(MimeMessage.RecipientType.TO,
			InternetAddress.parse(to_emailIds[i]));
			message.setSubject("Autoposter :  property id " + pID + " , posting on site id " + site_id + " using username " + username);
			message.setText("" + msg);

			Transport.send(message);

			System.out.println("sent email to "+to_emailIds[i]);
				}
			} catch (MessagingException e) {
			throw new RuntimeException(e);
			}
	}
//end of method sendmail()
	
//method to connect with the database
	void establishConn(){

		// Register JDBC driver
		try{
			Class.forName("com.mysql.jdbc.Driver");
			}catch (Exception ex) {
				flag=false;
				System.out.println("Error in JDBC Driver registration " + ex);
				sendmail("Error in JDBC Driver Registration : " + ex);
			}

				//Open a connection
				System.out.println("\nConnecting to database...");

				try{
					conn = DriverManager.getConnection(DB_URL,User_Name,Password);
				}catch (java.sql.SQLException s){
					flag=false;
					System.out.println(("Error in Database connection" + s));
					s.printStackTrace();
					StringWriter sw=new StringWriter();
					s.printStackTrace(new PrintWriter(sw));
					String stacktrace=sw.toString();
					sendmail("Error in Database connection \nStacktrace \n : " + stacktrace);
				}
	}
//end of method establishConn()	
	
//method to get the listing for post
	public boolean getListing()
	{
		//fetch posting details and update database to indicate that this posting has been attempted

		System.out.println("\n\nLets start !!!\n\nCreating statement to fetch listing ...");

		String sql1;

		DateFormat dateFormat3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		Date date = new Date();

		try{

			sql1 = "select * from posting_queue3 where posting_status <>'posted' and attempts<30 and posting_date <='"
					+dateFormat3.format(date)+ "'LIMIT 1 offset 0";
			System.out.println("\n" + sql1);
			PreparedStatement sqlQuery1 = conn.prepareStatement(sql1);
			sqlQuery1.setQueryTimeout(queryTimeout);

			ResultSet rs = sqlQuery1.executeQuery();
			
        //stop if no listings in posting_queue for post
			if(!rs.next()){
				System.out.println("No listing found in  posting queue for post");
				flag=false;
				sendmail("No listing found in posting queue");
				return(flag);
			}
			rs.beforeFirst(); //moves the cursor to the front of this ResultSet Object
			
		//Extract data from result set
            
			while(rs.next()){

				
				site_id = rs.getInt("site_id");

				pID = rs.getString("property_id");

				username = rs.getString("username");

				password = rs.getString("password");

				postingStatus = rs.getString("posting_status");

				uid = rs.getInt("uid");

			}

			
		System.out.println("\nPosting "+pID +" having posting status = " + postingStatus + " with " + "username "+ username + " and password " + password + " on site id " + site_id);

			//Clean-up environment
			sqlQuery1.close();
			rs.close();

			String sql2;
			sql2 = "update posting_queue3 set attempts = attempts +1  where uid= '"+uid+"' and property_id=? ";

			System.out.println(sql2 + "\n? = " + pID);

			System.out.println("\nUpdating 'attempts' in posting_queue ");

			PreparedStatement sqlQuery2 = conn.prepareStatement(sql2);
			sqlQuery2.setString(1, pID);
			sqlQuery2.setQueryTimeout(queryTimeout);

			sqlQuery2.executeUpdate();
			sqlQuery2.close();
		}
		catch(Exception ex)
		{
			System.out.println("Error in get listing for post\n"+ex);
			ex.printStackTrace();
			StringWriter sw=new StringWriter();
			ex.printStackTrace(new PrintWriter(sw));
			String stacktrace=sw.toString();
			sendmail("Error while fetching listing deatils\n Stacktrace\n"+stacktrace);
		}
		return flag;
	}
//end of getListing
	
public int getPropertyDetails()
{
	try
	{
	
		//fatching project details
		String sql3;
		sql3= "SELECT  p.project_id ,p.location_id, p.study_room, p.balconies, p.servant_rooms,"
				+"p.comes_from,p.super_area, p.furnishing, p.facing, p.project_name, p.bathrooms,"
				+"p.possession_year, p.possession_date,p.price, p.bedrooms, p.property_class_id, "
				+"p.age_of_construction, p.title, p1.phase, p1.name" 
						 +" FROM fv_properties p "
		 				 +" LEFT JOIN fv_projects p1 on p1.id=p.project_id "
		 				 +" WHERE p.id  = ? and p.is_deleted = '0' and p.is_verified = '1'" ;	
	
				
		System.out.println("the query for fetching project deatils is:\n" +sql3);
		PreparedStatement sqlQuery3=conn.prepareStatement(sql3);
		sqlQuery3.setString(1, pID);
		sqlQuery3.setQueryTimeout(queryTimeout);
		ResultSet rs=sqlQuery3.executeQuery();
		if(!rs.next()){
		System.out.println("Error in fetching project details");
		flag=false;
		}
		else{
		//rs.beforeFirst();
		//Extract data from ResultSet Object
		//while(rs.next()){
	
			pjID = rs.getString("project_id");

			propClassId = rs.getInt("property_class_id");

			location_id = rs.getInt("location_id");

			price = rs.getInt("price");

			bedrooms = rs.getInt("bedrooms");

			bathrooms = rs.getInt("bathrooms");

			myTitle = rs.getString("title");

			age = rs.getString("age_of_construction");

			possessionYear = rs.getInt("possession_year");

			balcony = rs.getInt("balconies");

			study = rs.getInt("study_room");

			servantRoom = rs.getInt("servant_rooms");

			newOrResale = rs.getInt("comes_from");

			bArea = rs.getInt("super_area");

			furnishing = rs.getString("furnishing");

			facing = rs.getString("facing");

			possession_date = rs.getDate("possession_date");
			
			phase= rs.getString("phase");
			
			project_name=rs.getString("name");
							
		}	
				
		//clean environment
		 rs.close();
		 sqlQuery3.close();
		
		
		//fetching amenities
		String sql4;
		sql4 = "SELECT reserved_parking,visitor_parking,garbase_disposal_each_floor,"
				+ "water_supply_24x7,swimming_pool,gym,kids_play_area,community_party_area,water_treatment,park,sewage_treatment,campus_atm  from"
				+" fv_project_amenities where project_id='"+pjID+"'";
		 System.out.println("the query to fetch amenities is "+sql4);
		 PreparedStatement sqlQuery4=conn.prepareStatement(sql4);
		 sqlQuery4.setQueryTimeout(queryTimeout);
		 ResultSet rs4=sqlQuery4.executeQuery();
		 if(!rs4.next()){
			System.out.println("Error in fetching project amenities");
				flag=false;
			    return 0;
		 }
			
			 else{
			//rs4.beforeFirst();
			//while(rs4.next()){	 
			reserved_parking = rs4.getString("reserved_parking");
				
			visitors_parking = rs4.getString("visitor_parking");
				
			garbage = rs4.getString("garbase_disposal_each_floor");
				
			water_supply = rs4.getString("water_supply_24x7");
				
			swimming_pool = rs4.getString("swimming_pool");
				
			gym = rs4.getString("gym");
				
			kids_area = rs4.getString("kids_play_area");
				
			party_area = rs4.getString("community_party_area");
				
			water_treatment=rs4.getString("water_treatment");
				
			park=rs4.getString("park");
			
			sewage_treatment=rs4.getString("sewage_treatment");
				
			atm=rs4.getString("campus_atm");
				
		 }
		//clean environment
		 rs.close();
		 sqlQuery3.close();
		 rs4.close();
		 sqlQuery4.close();
	
		
		return 1;
		
	}catch(Exception ex){
	System.out.println("Error in fetching project details"+ex);
	ex.printStackTrace();
	StringWriter sw=new StringWriter();
	ex.printStackTrace(new PrintWriter(sw));
	sendmail("Error in fetching project details\nStacktrace is"+sw);
	return 0;
	}
	
	}
//end of method getDetils()

//method for displaying all project details 
	public void viewDetails(){
		System.out.println("\n\nproject id is\t"+pjID+"\nproperty class id is\t"+propClassId+"\nlocation id is\t"+location_id);
		System.out.println("price is\t"+price+"\nbedrooms\t"+bedrooms+"\nbathrooms \t"+bathrooms+"\narea is\t"+bArea);
		System.out.println("age os construction is\t"+age+"\nnew or resale\t"+newOrResale+"\npossession date is\t"+possession_date);
		System.out.println("phase of project is "+phase+" and project_name is "+project_name);
		System.out.println("reserved parking is "+reserved_parking+"\nvisitors parking is "+visitors_parking);
		System.out.println("space for garbage disposal is "+garbage+"\nservice for water supply is "+water_supply);
		System.out.println("swimming pool is "+swimming_pool+"\ngym is "+gym+"\nkids area for playing is "+kids_area);
		System.out.println("party area is "+party_area+"\nwater treatment facility is "+water_treatment);
		System.out.println("park is "+park+"\nsewage treatment is "+sewage_treatment+"\natm is "+atm);
	}

	public static void main(String[] args) {
		DatabaseOperation db=new DatabaseOperation();
		db.establishConn();
		flag=db.getListing();
		if(flag){
			if(db.getPropertyDetails()!=0){
				db.viewDetails();
			}
			
		}
		else
		{
			System.out.println("error");
		}
		
	}

}

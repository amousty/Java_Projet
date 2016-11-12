package be.mousty.utilitaire;

import java.sql.Date;

//CLASSE UTILISEE POUR RECUPERER LID DE LA PERSONNE
public class ComboItem
{
	private String 	key;
	private int 	value;
	private Date valueDate;

	public ComboItem(String key, int value)
	{
		this.key 	= key;
		this.value 	= value;
	}
	
	public ComboItem(String key, Date valueDate)
	{
		this.key 	= key;
		this.valueDate 	= valueDate;
	}

	@Override
	public String 	toString	() { return key; 		} 
	public String 	getKey		() { return key; 		}
	public int 		getValue	() { return value; 		}
	public Date 	getValueDate() { return valueDate; 	}
}
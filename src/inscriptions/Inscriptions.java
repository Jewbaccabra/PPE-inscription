package inscriptions;


import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Date;
import java.util.SortedSet;
import java.util.TreeSet;

import database.Connexion;

/**
 * Point d'entr�e dans l'application, un seul objet de type Inscription
 * permet de g�rer les comp�titions, candidats (de type equipe ou personne)
 * ainsi que d'inscrire des candidats à des comp�tition.
 */

public class Inscriptions implements Serializable
{
	private static final long serialVersionUID = -3095339436048473524L;
	private static final String FILE_NAME = "Inscriptions.srz";
	private static Inscriptions inscriptions;
	/*private static boolean initBdd = true;*/
	private static Connexion connexion;
	
	private SortedSet<Competition> competitions = new TreeSet<>();
	private SortedSet<Candidat> candidats = new TreeSet<>();

	private Inscriptions()
	{
	}
	
	/**
	 * Retourne les comp�titions.
	 * @return
	 */
	/*public static boolean getInitBdd()
	{
		return initBdd;
	}*/
	
	public SortedSet<Competition> getCompetitions()
	{
		return Collections.unmodifiableSortedSet(competitions);
	}
	
	public static Connexion getConnexion()
	{
		return connexion ;
	}
	
	public static void setConnexion(Connexion connexion)
	{
		Inscriptions.connexion = connexion;
	}
	
	/**
	 * Retourne tous les candidats (personnes et �quipes confondues).
	 * @return
	 */
	
	public SortedSet<Candidat> getCandidats()
	{
		return Collections.unmodifiableSortedSet(candidats);
	}

	/**
	 * Cr��e une comp�tition. Ceci est le seul moyen, il n'y a pas
	 * de constructeur public dans {@link Competition}.
	 * @param nom
	 * @param dateCompet
	 * @param enEquipe
	 * @return
	 */
	
	public Competition createCompetition(String nom, Date dateCompet, boolean enEquipe)
	{
		/*if(!getInitBdd())
		{*/
			getConnexion().AjouterCompetition(nom, dateCompet,enEquipe);
		/*}*/
		
		
		Competition competition = new Competition(this, nom, dateCompet, enEquipe);
		competitions.add(competition);
		return competition;
		
		
		
		
	}

	/**
	 * Cr��e une Candidat de type Personne. Ceci est le seul moyen, il n'y a pas
	 * de constructeur public dans {@link Personne}.

	 * @param nom
	 * @param prenom
	 * @param mail
	 * @return
	 */
	
	public Personne createPersonne(String nom, String prenom, String mail)
	{
		/*if(!getInitBdd())
		{*/
			getConnexion().AjouterPersonne(nom, prenom, mail);
		/*}*/
		
		Personne personne = new Personne(this, nom, prenom, mail);
		candidats.add(personne);
		return personne;
		
	}
	
	/**
	 * Cr�er un Candidat de type �quipe.
	 * @param nom
	 * @param prenom
	 * @param mail
	 * @return
	 */
	
	public Equipe createEquipe(String nom)
	{
		Equipe equipe = new Equipe(this, nom);
		candidats.add(equipe);
		return equipe;
	}
	
	public Equipe supprEquipe(String nom)
	{
		
		getConnexion();
		Connexion.SupprimerCandidat(nom);
		Equipe equipe = new Equipe(this, nom);
		candidats.remove(equipe);
		return equipe;
		
	}
	public Personne supprPers(String nom)
	{
		
		getConnexion();
		Connexion.SupprimerCandidat(nom);
		String prenom = "";
		String mail = "";
		Personne personne = new Personne(this, nom, prenom, mail);
		candidats.remove(personne);
		return personne;
		
	}
	
	void remove(Competition competition)
	{
		competitions.remove(competition);
	}
	
	void remove(Candidat candidat)
	{
		candidats.remove(candidat);
	}
	
	/**
	 * Retourne l'unique instance de cette classe.
	 * Crée cet objet s'il n'existe déjà.
	 * @return l'unique objet de type {@link Inscriptions}.
	 */
	
	public static Inscriptions getInscriptions()
	{
		
		if (inscriptions == null)
		{
			setConnexion(new Connexion());
		 inscriptions = new Inscriptions();
		
			
		}
		return inscriptions;
	}

	
	private static Inscriptions readObject()
	{
		ObjectInputStream ois = null;
		try
		{
			FileInputStream fis = new FileInputStream(FILE_NAME);
			ois = new ObjectInputStream(fis);
			return (Inscriptions)(ois.readObject());
		}
		catch (IOException | ClassNotFoundException e)
		{
			return null;
		}
		finally
		{
				try
				{
					if (ois != null)
						ois.close();
				} 
				catch (IOException e){}
		}	
	}
	
	/**
	 * Sauvegarde le gestionnaire pour qu'il soit ouvert automatiquement 
	 * lors d'une exécution ultérieure du programme.
	 * @throws IOException 
	 */
	
	public void sauvegarder() throws IOException
	{
		ObjectOutputStream oos = null;
		try
		{
			FileOutputStream fis = new FileOutputStream(FILE_NAME);
			oos = new ObjectOutputStream(fis);
			oos.writeObject(this);
		}
		catch (IOException e)
		{
			throw e;
		}
		finally
		{
			try
			{
				if (oos != null)
					oos.close();
			} 
			catch (IOException e){}
		}
	}
	
	@Override
	public String toString()
	{
		return "Candidats : " + getCandidats().toString()
			+ "\nCompetitions  " + getCompetitions().toString();
	}
	
}

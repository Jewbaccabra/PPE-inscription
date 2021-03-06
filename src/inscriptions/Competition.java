package inscriptions;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
//import java.util.Date;
import java.util.Set;
import java.util.TreeSet;

/**
 * Représente une compétition, c'est-à-dire un ensemble de candidats 
 * inscrits à un événement, les inscriptions sont closes à la date dateCloture.
 *
 */

public class Competition implements Comparable<Competition>, Serializable
{
	private static final long serialVersionUID = -2882150118573759729L;
	private Inscriptions inscriptions;
	private String nom;
	private Set<Candidat> candidats;//????????
	private Date dateCloture;
	private boolean enEquipe = false;

	Competition(Inscriptions inscriptions, String nom, Date dateCompet, boolean enEquipe)
	{
		this.enEquipe = enEquipe;
		this.inscriptions = inscriptions;
		this.nom = nom;
		this.dateCloture = dateCompet;
		candidats = new TreeSet<>();
	}
	
	/**
	 * Retourne le nom de la compétition.
	 * @return
	 */
	
	public String getNom()
	{
		return nom;
	}
	
	/**
	 * Retourne la date de cloture des inscriptions.
	 * @return
	 */
	
	public Date getDateCloture()
	{
		return dateCloture;
	}
	
	/**
	 * Est vrai si et seulement si les inscriptions sont réservées aux équipes.
	 * @return
	 */
	
	public boolean estEnEquipe()
	{
		return enEquipe;
	}
	
	/**
	 * Modifie la date de cloture des inscriptions. Il est possible de la reculer 
	 * mais pas de l'avancer.
	 * @param dateCloture
	 */
	
	public void setDateCloture(Date dateCloture)
	{
		// TODO vérifier que l'on avance pas la date.
		
		if (this.getDateCloture() == null)
			this.dateCloture = dateCloture;
		else if(dateCloture.after(this.getDateCloture()))
			this.dateCloture = dateCloture;
		else
			throw new DateInvalide();
	}
	
	/**
	 * TODO retourne true si les inscriptions sont toujours ouvertes.
	 * Dans le cas contraire retourne false.
	 */
	
	public boolean inscriptionEstOuverte ()
	{
		if (this.getDateCloture() == null)
			return true;
		Date today =  new Date();
		return !(this.getDateCloture().before(today));
	}
	/**
	 * Retourne l'ensemble des candidats inscrits.
	 * @return
	 */
	
	public Set<Candidat> getCandidats()
	{
		return Collections.unmodifiableSet(candidats);
	}
	
	/**
	 * Inscrit un candidat de type Personne à la compétition. Provoque une
	 * exception si la compétition est réservée aux équipes.
	 * @param personne
	 * @return
	 */
	
	public boolean add(Personne personne)
	{
		// TODO vérifier que la date de clôture n'est pas passée
		
		Date today = new Date();
		if(this.getDateCloture() == null)
		{
			personne.add(this);
			return candidats.add(personne);
		}
		else if(today.before(this.getDateCloture()))
		{
				if (enEquipe)
				{
					throw new RuntimeException();
				}	
		}
		personne.add(this);
		return candidats.add(personne);
	}

	/**
	 * Inscrit un candidat de type Equipe à la compétition. Provoque une
	 * exception si la compétition est réservée aux personnes.
	 * @param personne
	 * @return
	 */

	public boolean add(Equipe equipe)
	{
		// TODO vérifier que la date de clôture n'est pas passée
		
		Date today = new Date();
		if(this.getDateCloture() == null)
		{
			equipe.add(this);
			return candidats.add(equipe);
		}
		if(today.before(this.getDateCloture()))
		{
			if (!enEquipe)
			{
				throw new RuntimeException();
			}	
		}
		equipe.add(this);
		return candidats.add(equipe);
	}

	/**
	 * Désinscrit un candidat.
	 * @param candidat
	 * @return
	 */
	
	public boolean remove(Candidat candidat)
	{
		candidat.remove(this);
		return candidats.remove(candidat);
	}
	
	/**
	 * Supprime la compétition de l'application.
	 */
	
	public void delete()
	{
		for (Candidat candidat : candidats)
			remove(candidat);
		inscriptions.remove(this);
	}
	
	@Override
	public int compareTo(Competition o)
	{
		return getNom().compareTo(o.getNom());
	}
	
	@Override
	public String toString()
	{
		return getNom();
	}
}

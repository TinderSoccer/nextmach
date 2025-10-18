package com.nextmatch.app.teams

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.nextmatch.app.R

/**
 * Adaptador de RecyclerView para mostrar una lista de equipos disponibles
 * @param teams Lista de equipos a mostrar
 * @param onTeamClick Callback cuando se selecciona un equipo
 * @param onJoinClick Callback cuando se solicita unirse a un equipo
 */
class TeamsAdapter(
    private val teams: List<Team>,
    private val onTeamClick: (Team) -> Unit,
    private val onJoinClick: (Team) -> Unit
) : RecyclerView.Adapter<TeamsAdapter.TeamViewHolder>() {

    /**
     * ViewHolder para cada item de equipo
     */
    inner class TeamViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvTeamName: TextView = itemView.findViewById(R.id.tv_team_name)
        private val tvTeamLocation: TextView = itemView.findViewById(R.id.tv_team_location)
        private val rbTeamLevel: RatingBar = itemView.findViewById(R.id.rb_team_level)
        private val btnJoin: Button = itemView.findViewById(R.id.btn_join)

        fun bind(team: Team) {
            // Asignar datos del equipo
            tvTeamName.text = team.name
            tvTeamLocation.text = team.location
            rbTeamLevel.rating = team.level

            // Cargar imagen del equipo (si es necesario implementar con Glide o Coil)
            // Glide.with(ivTeamAvatar).load(team.avatarUrl).into(ivTeamAvatar)

            // Click en el item
            itemView.setOnClickListener {
                onTeamClick(team)
            }

            // Click en el botón de unirse
            btnJoin.setOnClickListener {
                onJoinClick(team)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TeamViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.item_team,
            parent,
            false
        )
        return TeamViewHolder(view)
    }

    override fun onBindViewHolder(holder: TeamViewHolder, position: Int) {
        holder.bind(teams[position])
    }

    override fun getItemCount(): Int = teams.size
}
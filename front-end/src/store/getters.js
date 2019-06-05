export const user = state => state.user;
// export const user = state => {
//   return { name: "Gerhard Jansen" };
// };

export const hasBoards = state => {
  // return true;
  return state.boards.length > 0;
};

export const personalBoards = state => {
  // return [
  //   {
  //     id: 1,
  //     name: "vuejs.spring-boot.mysql",
  //     description:
  //       "An implementation of TaskAgile application with Vue.js, Spring Boot, and MySQL"
  //   }
  // ];
  return state.boards.filter(board => board.teamId === 0);
};

export const teamBoards = state => {
  const teams = [];

  // return [
  //   {
  //     id: 1,
  //     name: "Sales & Marketing",
  //     boards: [
  //       {
  //         id: 2,
  //         name: "2018 Planning",
  //         description: "2018 sales & marketing planning"
  //       },
  //       {
  //         id: 3,
  //         name: "Ongoing Campaigns",
  //         description: "2018 ongoing marketing campaigns"
  //       }
  //     ]
  //   }
  // ];

  state.teams.forEach(team => {
    teams.push({
      id: team.id,
      name: team.name,
      boards: state.boards.filter(board => board.teamId === team.id)
    });
  });

  return teams;
};

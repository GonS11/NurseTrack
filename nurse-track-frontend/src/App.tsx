import React from 'react';
import UsersList from './components/UsersList';

const App: React.FC = () => {
  return (
    <div>
      <h1>Bienvenido a NurseTrack</h1>
      <UsersList />
    </div>
  );
};

export default App;

import React, { useEffect, useState } from 'react';
import axios from 'axios';

interface User {
  id: number;
  username: string;
  role: string;
}

const UsersList: React.FC = () => {
  const [users, setUsers] = useState<User[]>([]);

  const getUsers = async () => {
    try {
      const response = await axios.get('http://localhost:8080/api/users');
      setUsers(response.data); // Actualizamos el estado con los usuarios
    } catch (error) {
      console.error('Error fetching users:', error);
    }
  };

  useEffect(() => {
    getUsers(); // Llamamos a la función al montar el componente
  }, []);

  return (
    <div>
      <h1>Lista de Usuarios</h1>
      <ul>
        {users.map((user) => (
          <li key={user.id}>
            {user.username} - {user.role}
          </li>
        ))}
      </ul>
    </div>
  );
};

export default UsersList;

package com.piedrazul.Infrastructure.repository;

import java.util.List;

import com.piedrazul.Domain.entities.ClsUser;
import com.piedrazul.Domain.enums.Role;

/**
 * Defines the contract for user persistence operations.
 * <p>
 * This interface follows the Repository pattern, providing an abstraction
 * over data access for {@link ClsUser} entities. It allows the application
 * to perform CRUD operations without being coupled to a specific
 * persistence mechanism.
 * </p>
 *
 * <p>
 * Implementations of this interface may interact with different data
 * sources such as relational databases, NoSQL systems, or in-memory storage.
 * </p>
 * 
 * @author Henry Fernando Mulato Llanten
 */
public interface IUserRepository {

  /**
   * Persists a new user in the data source.
   *
   * @param user the user entity to be stored
   * @return the persisted user, potentially with updated state (e.g., generated
   *         ID)
   */
  ClsUser opSave(ClsUser user);

  /**
   * Deletes a user by its unique identifier.
   *
   * @param id unique identifier of the user
   * @return {@code true} if the user was successfully deleted, {@code false}
   *         otherwise
   */
  boolean opDelete(long id);

  /**
   * Updates an existing user in the data source.
   *
   * @param user the user entity with updated information
   * @return {@code true} if the update was successful, {@code false} otherwise
   */
  boolean opUpdate(ClsUser user);

  /**
   * Retrieves a user by its unique identifier.
   *
   * @param id unique identifier of the user
   * @return the corresponding {@link ClsUser}, or {@code null} if not found
   */
  ClsUser opGet(long id);

  /**
   * Retrieves all users from the data source.
   *
   * @return a list containing all users
   */
  List<ClsUser> opFindAll();

  /**
   * Verifies user credentials and returns the associated role.
   * <p>
   * This method is typically used for authentication purposes.
   * </p>
   *
   * @param username user's username
   * @param password user's password
   * @return the {@link Role} associated with the user if credentials are valid
   * @throws RuntimeException if the verification process fails or credentials are
   *                          invalid
   */
  Role opVerifyUser(String username, String password) throws RuntimeException;

  boolean opDeactivate(long id);
  boolean opExistsByUsername(String username);
  /** Verifica si existe el username, excluyendo al usuario con ese id (para permitir update sin cambio de username). */
  boolean opExistsByUsernameExcludingId(String username, long id);
  boolean opUpdateState(long id, int stateId);

}
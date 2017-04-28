/*******************************************************************************
 * Copyright (c) 2009, 2017 GreenVulcano ESB Open Source Project.
 * All rights reserved.
 *
 * This file is part of GreenVulcano ESB.
 *
 * GreenVulcano ESB is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * GreenVulcano ESB is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with GreenVulcano ESB. If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/

package it.greenvulcano.gvesb.rsh;

import it.greenvulcano.gvesb.internal.GVInternalException;

/**
 * Remote Shell Exeption
 *
 * @version 4.0.0 - Mar 2017
 * @author GreenVulcano Developer Team
 */
public class RSHException extends GVInternalException {
	
	private static final long serialVersionUID = 6728132081884280763L;

	/**
	 * Constructor
	 * 
	 * @param messageId
	 */
	public RSHException(String messageId) {
		super(messageId);
	}

	/**
	 * Constructor
	 * 
	 * @param idMessage
	 * @param params
	 */
	public RSHException(String idMessage, String[][] params) {
		super(idMessage, params);
	}

	/**
	 * Constructor
	 * 
	 * @param messageId
	 * @param cause
	 */
	public RSHException(String messageId, Throwable cause) {
		super(messageId, cause);
	}

	/**
	 * Constructor
	 * 
	 * @param idMessage
	 * @param params
	 * @param cause
	 */
	public RSHException(String idMessage, String[][] params, Throwable cause) {
		super(idMessage, params, cause);
	}
}

/**
 * Copyright {2016-2017} Paul Boese aka Aeronica
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.aeronica.mods.bard_mania.server.network;

public class NetworkMessageRuntimeException extends RuntimeException
{

    private static final long serialVersionUID = -9021184631369763016L;

    public NetworkMessageRuntimeException() {/* empty by design */}

    public NetworkMessageRuntimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace)
    {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public NetworkMessageRuntimeException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public NetworkMessageRuntimeException(String message)
    {
        super(message);
    }

    public NetworkMessageRuntimeException(Throwable cause)
    {
        super(cause);
    }

}

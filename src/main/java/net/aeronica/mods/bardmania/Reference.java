/*
 * Copyright 2018 Paul Boese a.k.a Aeronica
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package net.aeronica.mods.bardmania;

import net.aeronica.mods.bardmania.caps.IBardAction;
import net.aeronica.mods.bardmania.common.Util;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

public class Reference {
    public static final String MOD_ID = "bardmania";
    public static final String MOD_DOMAIN = MOD_ID + ":";
    public static final String MOD_NAME = "Bard Mania";
    public static final String MOD_VERSION = "{@MOD_VERSION}";
    public static final String MC_VERSION = "[1.12.2,1.13]";
    public static final String UPDATES = "https://gist.githubusercontent.com/Aeronica/XXX/raw/update.json";
    public static final String FINGERPRINT = "999640c365a8443393a1a21df2c0ede9488400e9";
    public static final String DEPENDENCIES = "required-after:obfuscate@[0.2.3,)";
    public static final String PROXY_CLIENT = "net.aeronica.mods.bardmania.proxy.ClientProxy";
    public static final String PROXY_SERVER = "net.aeronica.mods.bardmania.proxy.CommonProxy";

    @CapabilityInject(IBardAction.class)
    public static final Capability<IBardAction> BARD_ACTION_CAP = Util.nonNullInjected();
}

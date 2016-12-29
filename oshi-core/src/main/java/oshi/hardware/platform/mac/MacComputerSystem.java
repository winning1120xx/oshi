/**
 * Oshi (https://github.com/dblock/oshi)
 *
 * Copyright (c) 2010 - 2016 The Oshi Project Team
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Maintainers:
 * dblock[at]dblock[dot]org
 * widdis[at]gmail[dot]com
 * enrico.bianchi[at]gmail[dot]com
 *
 * Contributors:
 * https://github.com/dblock/oshi/graphs/contributors
 */
package oshi.hardware.platform.mac;

import oshi.hardware.common.AbstractComputerSystem;
import oshi.util.ExecutingCommand;

/**
 * Hardware data obtained by system_profiler
 * 
 * @author SchiTho1 @ Securiton AG
 */
final class MacComputerSystem extends AbstractComputerSystem {

    private static final String CMD_SYSTEM_PROFILER_SPHARDWARE_DATA_TYPE = "system_profiler SPHardwareDataType";

    MacComputerSystem() {

        init();
    }

    private void init() {

        setManufacturer("Apple Inc.");

        // $ system_profiler SPHardwareDataType
        // Hardware:
        //
        // Hardware Overview:
        //
        // Model Name: MacBook Pro
        // Model Identifier: MacBookPro8,2
        // Processor Name: Intel Core i7
        // Processor Speed: 2.3 GHz
        // Number of Processors: 1
        // Total Number of Cores: 4
        // L2 Cache (per Core): 256 KB
        // L3 Cache: 8 MB
        // Memory: 16 GB
        // Boot ROM Version: MBP81.0047.B2C
        // SMC Version (system): 1.69f4
        // Serial Number (system): C02FH4XYCB71
        // Hardware UUID: D92CE829-65AD-58FA-9C32-88968791B7BD
        // Sudden Motion Sensor:
        // State: Enabled

        String modelName = "";
        final String modelNameMarker = "Model Name:";
        String modelIdentifier = "";
        final String modelIdMarker = "Model Identifier:";
        String serialNumberSystem = "";
        final String serialNumMarker = "Serial Number (system):";

        // Populate name and ID
        for (final String checkLine : ExecutingCommand.runNative(CMD_SYSTEM_PROFILER_SPHARDWARE_DATA_TYPE)) {
            if (checkLine.contains(modelNameMarker)) {
                modelName = checkLine.split(modelNameMarker)[1].trim();
            }
            if (checkLine.contains(modelIdMarker)) {
                modelIdentifier = checkLine.split(modelIdMarker)[1].trim();
            }
            if (checkLine.contains(serialNumMarker)) {
                serialNumberSystem = checkLine.split(serialNumMarker)[1].trim();
            }
        }
        // Use name (id) if both available; else either one
        if (!modelName.isEmpty()) {
            if (!modelIdentifier.isEmpty()) {
                setModel(modelName + " (" + modelIdentifier + ")");
            } else {
                setModel(modelName);
            }
        } else {
            if (!modelIdentifier.isEmpty()) {
                setModel(modelIdentifier);
            }
        }
        // Populate serial #
        if (!serialNumberSystem.isEmpty()) {
            setSerialNumber(serialNumberSystem);
        }
    }
}
/**
 *    Copyright 2020-present  Shanghai Jinmu Information Technology Co., Ltd.
 *
 *    This program is free software: you can redistribute it and/or modify
 *    it under the terms of the Server Side Public License, version 1,
 *    as published by Shanghai Jinmu Information Technology Co., Ltd.(The name of the development team is Whaleal.)
 *
 *
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    Server Side Public License for more details.
 *
 *    You should have received a copy of the Server Side Public License
 *    along with this program. If not, see
 *    <http://www.whaleal.com/licensing/server-side-public-license>.
 *
 *    As a special exception, the copyright holders give permission to link the
 *    code of portions of this program with the OpenSSL library under certain
 *    conditions as described in each individual source file and distribute
 *    linked combinations including the program with the OpenSSL library. You
 *    must comply with the Server Side Public License in all respects for
 *    all of the code used other than as permitted herein. If you modify file(s)
 *    with this exception, you may extend this exception to your version of the
 *    file(s), but you are not obligated to do so. If you do not wish to do so,
 *    delete this exception statement from your version. If you delete this
 *    exception statement from all source files in the program, then also delete
 *    it in the license file.
 */
package com.whaleal.mars.util;

import javax.net.ServerSocketFactory;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.util.Random;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Simple utility methods for working with network sockets &mdash; for example,
 * for finding available ports on {@code localhost}.
 *
 * <p>Within this class, a TCP port refers to a port for a {@link ServerSocket};
 * whereas, a UDP port refers to a port for a {@link DatagramSocket}.
 */
public class SocketUtils {

    /**
     * The default minimum value for port ranges used when finding an available
     * socket port.
     */
    public static final int PORT_RANGE_MIN = 1024;

    /**
     * The default maximum value for port ranges used when finding an available
     * socket port.
     */
    public static final int PORT_RANGE_MAX = 65535;


    private static final Random random = new Random(System.nanoTime());


    /**
     * Although {@code SocketUtils} consists solely of static utility methods,
     * this constructor is intentionally {@code public}.
     * <h4>Rationale</h4>
     * <p>Static methods from this class may be invoked from within XML
     * configuration files using the Spring Expression Language (SpEL) and the
     * following syntax.
     * <pre><code>&lt;bean id="bean1" ... p:port="#{T(org.springframework.util.SocketUtils).findAvailableTcpPort(12000)}" /&gt;</code></pre>
     * If this constructor were {@code private}, you would be required to supply
     * the fully qualified class name to SpEL's {@code T()} function for each usage.
     * Thus, the fact that this constructor is {@code public} allows you to reduce
     * boilerplate configuration with SpEL as can be seen in the following example.
     * <pre><code>&lt;bean id="socketUtils" class="org.springframework.util.SocketUtils" /&gt;
     * &lt;bean id="bean1" ... p:port="#{socketUtils.findAvailableTcpPort(12000)}" /&gt;
     * &lt;bean id="bean2" ... p:port="#{socketUtils.findAvailableTcpPort(30000)}" /&gt;</code></pre>
     */
    public SocketUtils() {
    }


    /**
     * Find an available TCP port randomly selected from the range
     * [{@value #PORT_RANGE_MIN}, {@value #PORT_RANGE_MAX}].
     *
     * @return an available TCP port number
     * @throws IllegalStateException if no available port could be found
     */
    public static int findAvailableTcpPort() {
        return findAvailableTcpPort(PORT_RANGE_MIN);
    }

    /**
     * Find an available TCP port randomly selected from the range
     * [{@code minPort}, {@value #PORT_RANGE_MAX}].
     *
     * @param minPort the minimum port number
     * @return an available TCP port number
     * @throws IllegalStateException if no available port could be found
     */
    public static int findAvailableTcpPort(int minPort) {
        return findAvailableTcpPort(minPort, PORT_RANGE_MAX);
    }

    /**
     * Find an available TCP port randomly selected from the range
     * [{@code minPort}, {@code maxPort}].
     *
     * @param minPort the minimum port number
     * @param maxPort the maximum port number
     * @return an available TCP port number
     * @throws IllegalStateException if no available port could be found
     */
    public static int findAvailableTcpPort(int minPort, int maxPort) {
        return SocketType.TCP.findAvailablePort(minPort, maxPort);
    }

    /**
     * Find the requested number of available TCP ports, each randomly selected
     * from the range [{@value #PORT_RANGE_MIN}, {@value #PORT_RANGE_MAX}].
     *
     * @param numRequested the number of available ports to find
     * @return a sorted set of available TCP port numbers
     * @throws IllegalStateException if the requested number of available ports could not be found
     */
    public static SortedSet<Integer> findAvailableTcpPorts(int numRequested) {
        return findAvailableTcpPorts(numRequested, PORT_RANGE_MIN, PORT_RANGE_MAX);
    }

    /**
     * Find the requested number of available TCP ports, each randomly selected
     * from the range [{@code minPort}, {@code maxPort}].
     *
     * @param numRequested the number of available ports to find
     * @param minPort      the minimum port number
     * @param maxPort      the maximum port number
     * @return a sorted set of available TCP port numbers
     * @throws IllegalStateException if the requested number of available ports could not be found
     */
    public static SortedSet<Integer> findAvailableTcpPorts(int numRequested, int minPort, int maxPort) {
        return SocketType.TCP.findAvailablePorts(numRequested, minPort, maxPort);
    }

    /**
     * Find an available UDP port randomly selected from the range
     * [{@value #PORT_RANGE_MIN}, {@value #PORT_RANGE_MAX}].
     *
     * @return an available UDP port number
     * @throws IllegalStateException if no available port could be found
     */
    public static int findAvailableUdpPort() {
        return findAvailableUdpPort(PORT_RANGE_MIN);
    }

    /**
     * Find an available UDP port randomly selected from the range
     * [{@code minPort}, {@value #PORT_RANGE_MAX}].
     *
     * @param minPort the minimum port number
     * @return an available UDP port number
     * @throws IllegalStateException if no available port could be found
     */
    public static int findAvailableUdpPort(int minPort) {
        return findAvailableUdpPort(minPort, PORT_RANGE_MAX);
    }

    /**
     * Find an available UDP port randomly selected from the range
     * [{@code minPort}, {@code maxPort}].
     *
     * @param minPort the minimum port number
     * @param maxPort the maximum port number
     * @return an available UDP port number
     * @throws IllegalStateException if no available port could be found
     */
    public static int findAvailableUdpPort(int minPort, int maxPort) {
        return SocketType.UDP.findAvailablePort(minPort, maxPort);
    }

    /**
     * Find the requested number of available UDP ports, each randomly selected
     * from the range [{@value #PORT_RANGE_MIN}, {@value #PORT_RANGE_MAX}].
     *
     * @param numRequested the number of available ports to find
     * @return a sorted set of available UDP port numbers
     * @throws IllegalStateException if the requested number of available ports could not be found
     */
    public static SortedSet<Integer> findAvailableUdpPorts(int numRequested) {
        return findAvailableUdpPorts(numRequested, PORT_RANGE_MIN, PORT_RANGE_MAX);
    }

    /**
     * Find the requested number of available UDP ports, each randomly selected
     * from the range [{@code minPort}, {@code maxPort}].
     *
     * @param numRequested the number of available ports to find
     * @param minPort      the minimum port number
     * @param maxPort      the maximum port number
     * @return a sorted set of available UDP port numbers
     * @throws IllegalStateException if the requested number of available ports could not be found
     */
    public static SortedSet<Integer> findAvailableUdpPorts(int numRequested, int minPort, int maxPort) {
        return SocketType.UDP.findAvailablePorts(numRequested, minPort, maxPort);
    }


    private enum SocketType {

        TCP {
            @Override
            protected boolean isPortAvailable(int port) {
                try {
                    ServerSocket serverSocket = ServerSocketFactory.getDefault().createServerSocket(
                            port, 1, InetAddress.getByName("localhost"));
                    serverSocket.close();
                    return true;
                } catch (Exception ex) {
                    return false;
                }
            }
        },

        UDP {
            @Override
            protected boolean isPortAvailable(int port) {
                try {
                    DatagramSocket socket = new DatagramSocket(port, InetAddress.getByName("localhost"));
                    socket.close();
                    return true;
                } catch (Exception ex) {
                    return false;
                }
            }
        };

        /**
         * Determine if the specified port for this {@code SocketType} is
         * currently available on {@code localhost}.
         */
        protected abstract boolean isPortAvailable(int port);

        /**
         * Find a pseudo-random port number within the range
         * [{@code minPort}, {@code maxPort}].
         *
         * @param minPort the minimum port number
         * @param maxPort the maximum port number
         * @return a random port number within the specified range
         */
        private int findRandomPort(int minPort, int maxPort) {
            int portRange = maxPort - minPort;
            return minPort + random.nextInt(portRange + 1);
        }

        /**
         * Find an available port for this {@code SocketType}, randomly selected
         * from the range [{@code minPort}, {@code maxPort}].
         *
         * @param minPort the minimum port number
         * @param maxPort the maximum port number
         * @return an available port number for this socket type
         * @throws IllegalStateException if no available port could be found
         */
        int findAvailablePort(int minPort, int maxPort) {
            Assert.isTrue(minPort > 0, "'minPort' must be greater than 0");
            Assert.isTrue(maxPort >= minPort, "'maxPort' must be greater than or equal to 'minPort'");
            Assert.isTrue(maxPort <= PORT_RANGE_MAX, "'maxPort' must be less than or equal to " + PORT_RANGE_MAX);

            int portRange = maxPort - minPort;
            int candidatePort;
            int searchCounter = 0;
            do {
                if (searchCounter > portRange) {
                    throw new IllegalStateException(String.format(
                            "Could not find an available %s port in the range [%d, %d] after %d attempts",
                            name(), minPort, maxPort, searchCounter));
                }
                candidatePort = findRandomPort(minPort, maxPort);
                searchCounter++;
            }
            while (!isPortAvailable(candidatePort));

            return candidatePort;
        }

        /**
         * Find the requested number of available ports for this {@code SocketType},
         * each randomly selected from the range [{@code minPort}, {@code maxPort}].
         *
         * @param numRequested the number of available ports to find
         * @param minPort      the minimum port number
         * @param maxPort      the maximum port number
         * @return a sorted set of available port numbers for this socket type
         * @throws IllegalStateException if the requested number of available ports could not be found
         */
        SortedSet<Integer> findAvailablePorts(int numRequested, int minPort, int maxPort) {
            Assert.isTrue(minPort > 0, "'minPort' must be greater than 0");
            Assert.isTrue(maxPort > minPort, "'maxPort' must be greater than 'minPort'");
            Assert.isTrue(maxPort <= PORT_RANGE_MAX, "'maxPort' must be less than or equal to " + PORT_RANGE_MAX);
            Assert.isTrue(numRequested > 0, "'numRequested' must be greater than 0");
            Assert.isTrue((maxPort - minPort) >= numRequested,
                    "'numRequested' must not be greater than 'maxPort' - 'minPort'");

            SortedSet<Integer> availablePorts = new TreeSet<>();
            int attemptCount = 0;
            while ((++attemptCount <= numRequested + 100) && availablePorts.size() < numRequested) {
                availablePorts.add(findAvailablePort(minPort, maxPort));
            }

            if (availablePorts.size() != numRequested) {
                throw new IllegalStateException(String.format(
                        "Could not find %d available %s ports in the range [%d, %d]",
                        numRequested, name(), minPort, maxPort));
            }

            return availablePorts;
        }
    }

}
